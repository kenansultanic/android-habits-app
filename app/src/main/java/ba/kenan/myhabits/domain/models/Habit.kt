package ba.kenan.myhabits.domain.models

import java.sql.Timestamp
import java.util.Date

data class Habit(
    val name: String = "",
    val tags: List<String> = emptyList(),
    val isArchived: Boolean = false,
    val createdAt: Timestamp = Timestamp(Date().time),
    val frequency: Frequency = Frequency(),
    //val history: DocumentReference? = null
)

data class Frequency(
    val type: String = "",
    val days: List<Int> = emptyList()
)
