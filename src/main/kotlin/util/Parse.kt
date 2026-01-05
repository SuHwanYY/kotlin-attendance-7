package util

import repository.CrewRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun parseMenuNumber(raw: String): String {
    val s = raw.trim()
    if (s !in setOf("1", "2", "3", "4", "Q")) {
        throw IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.")
    }
    return s
}

fun parseInputNickname(raw: String, crewRepository: CrewRepository): String {
    val nickname = raw.trim()
    if (nickname.isEmpty()) {
        throw IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.")
    }
    if (!crewRepository.exists(nickname)) {
        throw IllegalArgumentException("[ERROR] 등록되지 않은 닉네임입니다.")
    }
    return nickname
}

fun parseDayOfMonth(raw: String): Int {
    val s = raw.trim()
    val day = s.toIntOrNull() ?: throw IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.")
    if (day !in 1..31) throw IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.")
    return day
}

fun parseInputTime(raw: String): LocalTime {
    val s = raw.trim()
    return try {
        LocalTime.parse(s, TIME_FORMATTER)
    } catch (e: Exception) {
        throw IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.")
    }
}