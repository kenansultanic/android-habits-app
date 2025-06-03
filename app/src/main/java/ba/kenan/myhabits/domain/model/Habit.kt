package ba.kenan.myhabits.domain.model

import java.util.Date

data class Habit(
    val id: String = "",
    val name: String = "",
    val tags: List<String> = emptyList(),
    val frequency: Frequency = Frequency(),
    val isArchived: Boolean = false,
    val createdAt: Date = Date(),
    val history: Map<String, Boolean> = emptyMap()
)

data class Frequency(
    val type: String = "",
    val days: List<Int> = emptyList()
)
