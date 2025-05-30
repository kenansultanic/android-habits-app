package ba.kenan.myhabits.domain.models

import java.util.Date

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val birthDate: Date = Date(),
    val timezone: String = "",
    val joinedOn: Date = Date(),
    val habits: List<Habit> = emptyList()
)
