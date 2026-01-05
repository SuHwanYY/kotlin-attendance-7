package view

import camp.nextstep.edu.missionutils.Console
import repository.CrewRepository
import util.parseDayOfMonth
import util.parseInputNickname
import util.parseInputTime
import util.parseMenuNumber
import java.time.LocalTime

class InputView(private val crewRepository: CrewRepository) {

    fun readMenuNumber(): String {
        return parseMenuNumber(Console.readLine())
    }

    fun readCheckNickname(): String {
        println("닉네임을 입력해 주세요.")
        return parseInputNickname(Console.readLine(), crewRepository)
    }

    fun readCheckTime(): LocalTime {
        println("등교 시간을 입력해 주세요.")
        return parseInputTime(Console.readLine())
    }

    fun readModifyNickname(): String {
        println("닉네임을 입력해 주세요.")
        return parseInputNickname(Console.readLine(), crewRepository)
    }

    fun readModifyDayOfMonth(): Int {
        println("수정할 날짜를 입력해 주세요.")
        return parseDayOfMonth(Console.readLine())
    }

    fun readModifyTime(): LocalTime {
        println("수정할 시간을 입력해 주세요.")
        return parseInputTime(Console.readLine())
    }

    fun readCrewNickname(): String {
        println("닉네임을 입력해 주세요.")
        return parseInputNickname(Console.readLine(), crewRepository)
    }
}