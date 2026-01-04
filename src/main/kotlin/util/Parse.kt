package util

import repository.CrewRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// 메뉴 번호 입력 파싱
fun parseMenuNumber(raw: String): String {
    val menuNum = raw.trim()
    if(menuNum !in setOf("1", "2", "3", "Q")) throw IllegalArgumentException("[ERROR] 잘못된 메뉴 입력입니다.")

    return menuNum
}

// 닉네임 파싱
fun parseInputNickname(raw: String, crewRepository: CrewRepository): String {
    val nickname = raw.trim()
    if (nickname.isEmpty()) {
        throw IllegalArgumentException("[ERROR] 닉네임을 입력해 주세요.")
    }
    if (!crewRepository.exists(nickname)) {
        throw IllegalArgumentException("[ERROR] 등록되지 않은 닉네임입니다.")
    }
    return nickname
}

// 시간 입력 포맷팅 및 파싱
private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun parseInputTime(raw: String): LocalTime {
    val s = raw.trim()
    return try {
        LocalTime.parse(s, TIME_FORMATTER)
    } catch (e: Exception) {
        throw IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.")
    }
}