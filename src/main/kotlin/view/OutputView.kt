package view

import camp.nextstep.edu.missionutils.DateTimes
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
}