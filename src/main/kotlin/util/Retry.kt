package util

inline fun <T> retry(block: () -> T) : T {
    while(true) {
        try {
            return block()
        } catch (e: IllegalArgumentException) {
            println(e.message ?: "[ERROR] 잘못된 입력입니다.")
        }
    }
}