package ba.kenan.myhabits.presentation.utils

import ba.kenan.myhabits.domain.model.Habit
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun calculateHabitStatistics(habits: List<Habit>): Pair<Int, Int> {
    val allHistory = habits.flatMap { it.history.entries }

    val completedDates = allHistory.filter { it.value }.map { it.key }.toSet()
    val totalCompletedDays = completedDates.size

    val streak = calculateCurrentStreak(completedDates)

    return Pair(totalCompletedDays, streak)
}

private fun calculateCurrentStreak(dates: Set<String>): Int {
    val formatter = DateTimeFormatter.ISO_DATE
    var current = LocalDate.now()
    var streak = 0

    while (dates.contains(current.format(formatter))) {
        streak++
        current = current.minusDays(1)
    }
    return streak
}

private fun estimateExpectedCompletions(habits: List<Habit>): Int {
    val today = LocalDate.now()
    var count = 0

    habits.forEach { habit ->
        val start = habit.createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val days = generateSequence(start) { it.plusDays(1) }
            .takeWhile { !it.isAfter(today) }
            .filter { date ->
                val dayOfWeek = date.dayOfWeek.value % 7
                when (habit.frequency.type.lowercase()) {
                    "daily" -> true
                    "weekly" -> habit.frequency.days.firstOrNull() == dayOfWeek
                    "custom" -> habit.frequency.days.contains(dayOfWeek)
                    else -> false
                }
            }
            .toList()
        count += days.size
    }
    return count
}
