package view

import camp.nextstep.edu.missionutils.Console
import camp.nextstep.edu.missionutils.DateTimes
import model.AttendanceCheck
import repository.CrewRepository
import util.parseInputNickname
import util.parseInputTime
import util.parseMenuNumber
import java.time.LocalDateTime
import java.time.LocalTime

class InputView(private val crewRepository: CrewRepository) {

    fun readMenuNumber(): String {
        return parseMenuNumber(Console.readLine())
    }

    fun readCheckNickname(): String {
        println("닉네임을 입력해 주세요.")
        val nickname = parseInputNickname(Console.readLine(), crewRepository)

        return nickname
    }

    fun readCheckTime(): LocalTime {
        println("등교 시간을 입력해 주세요.")
        val time = parseInputTime(Console.readLine())

        return time
    }

}