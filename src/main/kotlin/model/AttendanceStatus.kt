package model

enum class AttendanceStatus(val label: String) {
    ATTEND("출석"),
    LATE("지각"),
    ABSENT("결석"),
}