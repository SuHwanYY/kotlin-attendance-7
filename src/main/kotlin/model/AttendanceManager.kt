package model

import camp.nextstep.edu.missionutils.DateTimes
import repository.AttendanceRepository
import repository.CrewRepository
import java.time.LocalDate
import java.time.LocalTime

data class DailyAttendance(val date: LocalDate, val time: LocalTime?, val status: AttendanceStatus)

data class AttendanceSummary(
    val attend: Int,
    val late: Int,
    val absent: Int,
    val riskLevel: RiskLevel,
)

data class RiskEntry(
    val nickname: String,
    val late: Int,
    val absent: Int,
    val convertedAbsent: Int,
    val riskLevel: RiskLevel,
)

class AttendanceManager(
    private val attendanceRepository: AttendanceRepository,
    private val crewRepository: CrewRepository,
) {

    fun validateTodayCanCheck(today: LocalDate = DateTimes.now().toLocalDate()) {
        validateTodayIsSchoolDay(today)
    }

    fun checkAttendance(
        nickname: String,
        time: LocalTime,
        today: LocalDate = DateTimes.now().toLocalDate(),
    ): DailyAttendance {
        validateTodayIsSchoolDay(today)
        validateCampusOperating(time)

        val ok = attendanceRepository.add(nickname, today, time)
        if (!ok) {
            throw IllegalArgumentException("[ERROR] 이미 출석을 확인하였습니다. 필요한 경우 수정 기능을 이용해 주세요.")
        }

        val status = AttendancePolicy.status(today, time)
        return DailyAttendance(today, time, status)
    }

    fun modifyAttendance(
        nickname: String,
        dayOfMonth: Int,
        newTime: LocalTime,
        today: LocalDate = DateTimes.now().toLocalDate(),
    ): Pair<DailyAttendance, DailyAttendance> {
        val target = LocalDate.of(today.year, today.month, dayOfMonth)

        if (!AttendancePolicy.isSchoolDay(target)) {
            throw IllegalArgumentException("[ERROR] ${formatDateKorean(target)}은 등교일이 아닙니다.")
        }
        if (!target.isBefore(today)) {
            throw IllegalArgumentException("[ERROR] 아직 수정할 수 없습니다.")
        }
        validateCampusOperating(newTime)

        val prevTime = attendanceRepository.findTime(nickname, target)
        val before = DailyAttendance(target, prevTime, AttendancePolicy.status(target, prevTime))

        attendanceRepository.update(nickname, target, newTime)
        val after = DailyAttendance(target, newTime, AttendancePolicy.status(target, newTime))

        return before to after
    }

    fun buildCrewReport(
        nickname: String,
        today: LocalDate = DateTimes.now().toLocalDate(),
    ): Pair<List<DailyAttendance>, AttendanceSummary> {
        val startOfMonth = LocalDate.of(today.year, today.month, 1)
        val end = today.minusDays(1)

        val daily = mutableListOf<DailyAttendance>()
        var attend = 0
        var late = 0
        var absent = 0

        var d = startOfMonth
        while (!d.isAfter(end)) {
            if (AttendancePolicy.isSchoolDay(d)) {
                val time = attendanceRepository.findTime(nickname, d)
                val status = AttendancePolicy.status(d, time)
                daily += DailyAttendance(d, time, status)

                when (status) {
                    AttendanceStatus.ATTEND -> attend++
                    AttendanceStatus.LATE -> late++
                    AttendanceStatus.ABSENT -> absent++
                }
            }
            d = d.plusDays(1)
        }

        val risk = calcRiskLevel(absent, late)
        return daily to AttendanceSummary(attend, late, absent, risk)
    }

    fun buildRiskReport(today: LocalDate = DateTimes.now().toLocalDate()): List<RiskEntry> {
        val result = mutableListOf<RiskEntry>()

        for (nickname in crewRepository.findAll()) {
            val (_, summary) = buildCrewReport(nickname, today)
            val convertedAbsent = summary.absent + (summary.late / 3)
            val risk = calcRiskLevel(summary.absent, summary.late)
            if (risk != RiskLevel.NONE) {
                result += RiskEntry(
                    nickname = nickname,
                    late = summary.late,
                    absent = summary.absent,
                    convertedAbsent = convertedAbsent,
                    riskLevel = risk,
                )
            }
        }

        // 제적 > 면담 > 경고, 그 다음 결석(환산) 많은 순으로
        return result.sortedWith(
            compareByDescending<RiskEntry> { it.riskLevel.ordinal }
                .thenByDescending { it.convertedAbsent }
                .thenByDescending { it.late }
                .thenBy { it.nickname }
        )
    }

    private fun calcRiskLevel(absent: Int, late: Int): RiskLevel {
        val convertedAbsent = absent + (late / 3)
        return when {
            convertedAbsent > 5 -> RiskLevel.EXPEL
            convertedAbsent >= 3 -> RiskLevel.INTERVIEW
            convertedAbsent >= 2 -> RiskLevel.WARNING
            else -> RiskLevel.NONE
        }
    }

    private fun validateTodayIsSchoolDay(today: LocalDate) {
        if (!AttendancePolicy.isSchoolDay(today)) {
            throw IllegalArgumentException("[ERROR] ${formatDateKorean(today)}은 등교일이 아닙니다.")
        }
    }

    private fun validateCampusOperating(time: LocalTime) {
        if (!AttendancePolicy.isCampusOperating(time)) {
            throw IllegalArgumentException("[ERROR] 캠퍼스 운영 시간에만 출석이 가능합니다.")
        }
    }

    private fun formatDateKorean(date: LocalDate): String {
        val month = String.format("%02d", date.monthValue)
        val day = String.format("%02d", date.dayOfMonth)
        val dow = when (date.dayOfWeek) {
            java.time.DayOfWeek.MONDAY -> "월요일"
            java.time.DayOfWeek.TUESDAY -> "화요일"
            java.time.DayOfWeek.WEDNESDAY -> "수요일"
            java.time.DayOfWeek.THURSDAY -> "목요일"
            java.time.DayOfWeek.FRIDAY -> "금요일"
            java.time.DayOfWeek.SATURDAY -> "토요일"
            java.time.DayOfWeek.SUNDAY -> "일요일"
        }
        return "${month}월 ${day}일 ${dow}"
    }
}