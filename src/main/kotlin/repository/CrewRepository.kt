package repository

import java.io.BufferedReader
import java.io.InputStreamReader

class CrewRepository(resourcePath: String) {
    private val crews: Set<String> = loadCrewNames(resourcePath)

    fun exists(nickname: String): Boolean = nickname in crews

    fun findAll(): List<String> = crews.toList().sorted()

    private fun loadCrewNames(resourcePath: String): Set<String> {
        val inputStream = requireNotNull(javaClass.getResourceAsStream(resourcePath)) {
            "리소스 파일을 찾을 수 없습니다: $resourcePath"
        }

        BufferedReader(InputStreamReader(inputStream)).use { br ->
            return br.lineSequence()
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .filterNot { it.startsWith("nickname", ignoreCase = true) } // 헤더 제거
                .map { line -> line.split(",")[0].trim() }
                .toSet()
        }
    }
}