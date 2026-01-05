package model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

object AttendancePolicy {
    private val campusOpen: LocalTime = LocalTime.of(8, 0)
    private val campusClose: LocalTime = LocalTime.of(23, 0)

    // 최소한 크리스마스 정도만 넣어둠(필요하면 더 추가)
    private fun holidaysOf(year: Int): Set<LocalDate> = setOf(
        LocalDate.of(year, 12, 25),
    )

    fun isCampusOperating(time: LocalTime): Boolean =
        !time.isBefore(campusOpen) && !time.isAfter(campusClose)

    fun isSchoolDay(date: LocalDate): Boolean {
        val dow = date.dayOfWeek
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) return false
        if (date in holidaysOf(date.year)) return false
        return true
    }

    fun classStartTime(date: LocalDate): LocalTime =
        if (date.dayOfWeek == DayOfWeek.MONDAY) LocalTime.of(13, 0) else LocalTime.of(10, 0)

    fun status(date: LocalDate, arrival: LocalTime?): AttendanceStatus {
        if (arrival == null) return AttendanceStatus.ABSENT

        val start = classStartTime(date)
        val minutesLate = java.time.Duration.between(start, arrival).toMinutes()

        return when {
            minutesLate > 30 -> AttendanceStatus.ABSENT
            minutesLate > 5 -> AttendanceStatus.LATE
            else -> AttendanceStatus.ATTEND
        }
    }
}