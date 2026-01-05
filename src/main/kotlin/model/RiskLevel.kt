package model

enum class RiskLevel(val label: String) {
    NONE(""),
    WARNING("경고"),
    INTERVIEW("면담"),
    EXPEL("제적"),
}