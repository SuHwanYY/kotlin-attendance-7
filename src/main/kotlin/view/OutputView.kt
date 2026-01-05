package view

import camp.nextstep.edu.missionutils.DateTimes
import model.AttendanceSummary
import model.DailyAttendance
import model.RiskEntry
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class OutputView {
    fun printFeatureSelect() {
        val today: LocalDate = DateTimes.now().toLocalDate()
        val datePart = today.format(DateTimeFormatter.ofPattern("MM월 dd일"))
        val dayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)

        println("오늘은 ${datePart} ${dayOfWeek}입니다. 기능을 선택해 주세요.")
        println("1.출석 확인 2. 출석 수정 3. 크루별 출석 기록 확인 4. 제적 위험자 확인 Q. 종료")
    }

    fun printCheckedResult(att: DailyAttendance) {
        val datePart = att.date.format(DateTimeFormatter.ofPattern("MM월 dd일"))
        val dayOfWeek = att.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
        val timePart = att.time!!.format(DateTimeFormatter.ofPattern("HH:mm"))
        println("${datePart} ${dayOfWeek} ${timePart} (${att.status.label})")
    }

    fun printModifyResult(before: DailyAttendance, after: DailyAttendance) {
        val datePart = before.date.format(DateTimeFormatter.ofPattern("MM월 dd일"))
        val dayOfWeek = before.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)

        val beforeTime = before.time?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "--:--"
        val afterTime = after.time?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "--:--"

        println("${datePart} ${dayOfWeek} ${beforeTime} (${before.status.label}) -> ${afterTime} (${after.status.label}) 수정 완료!")
    }

    fun printCrewReport(days: List<DailyAttendance>, summary: AttendanceSummary) {
        val dateFormatter = DateTimeFormatter.ofPattern("MM월 dd일")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        for (d in days) {
            val datePart = d.date.format(dateFormatter)
            val dow = d.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
            val timePart = d.time?.format(timeFormatter) ?: "--:--"
            println("${datePart} ${dow} ${timePart} (${d.status.label})")
        }

        println("출석: ${summary.attend}회")
        println("지각: ${summary.late}회")
        println("결석: ${summary.absent}회")
        if (summary.riskLevel.label.isNotEmpty()) {
            println(summary.riskLevel.label)
        }
    }

    fun printRiskReport(list: List<RiskEntry>) {
        println("제적 위험자 조회 결과")
        if (list.isEmpty()) {
            println("제적 위험자가 없습니다.")
            return
        }
        list.forEach { e ->
            println("- ${e.nickname}: 결석 ${e.absent}회, 지각 ${e.late}회 (${e.riskLevel.label})")
        }
    }
}