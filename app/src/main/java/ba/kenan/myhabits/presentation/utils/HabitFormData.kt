package ba.kenan.myhabits.presentation.utils

data class HabitFormData(
    val name: String,
    val tags: List<String>,
    val frequencyType: String,
    val frequencyDays: List<Int>
)
