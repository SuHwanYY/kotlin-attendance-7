package repository

import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AttendanceRepository(resourcePath: String) {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    // nickname -> (date -> time)
    private val records: MutableMap<String, MutableMap<LocalDate, LocalTime>> = mutableMapOf()

    init {
        load(resourcePath)
    }

    fun findTime(nickname: String, date: LocalDate): LocalTime? =
        records[nickname]?.get(date)

    fun add(nickname: String, date: LocalDate, time: LocalTime): Boolean {
        val byDate = records.getOrPut(nickname) { mutableMapOf() }
        if (byDate.containsKey(date)) return false
        byDate[date] = time
        return true
    }

    fun update(nickname: String, date: LocalDate, time: LocalTime): LocalTime? {
        val byDate = records.getOrPut(nickname) { mutableMapOf() }
        val prev = byDate[date]
        byDate[date] = time
        return prev
    }

    private fun load(resourcePath: String) {
        val inputStream = requireNotNull(javaClass.getResourceAsStream(resourcePath)) {
            "리소스 파일을 찾을 수 없습니다: $resourcePath"
        }

        BufferedReader(InputStreamReader(inputStream)).use { br ->
            br.lineSequence()
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .filterNot { it.startsWith("nickname", ignoreCase = true) }
                .forEach { line ->
                    val parts = line.split(",")
                    if (parts.size < 2) return@forEach
                    val nickname = parts[0].trim()
                    val dt = LocalDateTime.parse(parts[1].trim(), formatter)
                    records.getOrPut(nickname) { mutableMapOf() }[dt.toLocalDate()] = dt.toLocalTime()
                }
        }
    }
}