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
