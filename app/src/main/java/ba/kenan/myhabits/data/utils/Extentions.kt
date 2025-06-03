package ba.kenan.myhabits.data.utils

import ba.kenan.myhabits.domain.model.Frequency
import ba.kenan.myhabits.domain.model.Habit
import java.util.Date

fun Map<String, Any>.toHabit(id: String): Habit {
    val name = this["name"] as? String ?: ""
    val tags = this["tags"] as? List<String> ?: emptyList()
    val isArchived = this["isArchived"] as? Boolean ?: false
    val createdAt = this["createdAt"] as? Date ?: Date()

    val frequencyMap = this["frequency"] as? Map<*, *>
    val frequencyType = frequencyMap?.get("type") as? String ?: ""
    val frequencyDays = (frequencyMap?.get("days") as? List<*>)?.mapNotNull {
        when (it) {
            is Long -> it.toInt()
            is Int -> it
            else -> null
        }
    } ?: emptyList()

    val history = this["history"] as? Map<String, Boolean> ?: emptyMap()

    return Habit(
        id = id,
        name = name,
        tags = tags,
        isArchived = isArchived,
        createdAt = createdAt,
        frequency = Frequency(type = frequencyType, days = frequencyDays),
        history = history
    )
}
