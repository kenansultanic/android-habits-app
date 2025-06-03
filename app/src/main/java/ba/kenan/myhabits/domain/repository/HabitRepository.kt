package ba.kenan.myhabits.domain.repository

import ba.kenan.myhabits.domain.model.Habit
import com.google.firebase.firestore.Source

interface HabitRepository {

    suspend fun getHabitsForUser(userId: String, source: Source): Result<List<Habit>>

    suspend fun addHabit(
        userId: String,
        name: String,
        tags: List<String>,
        frequencyType: String,
        frequencyDays: List<Int>
    ): Result<Unit>

    suspend fun updateHabit(
        userId: String,
        habitId: String,
        name: String,
        tags: List<String>,
        frequencyType: String,
        frequencyDays: List<Int>,
        isArchived: Boolean
    ): Result<Unit>

    suspend fun deleteHabit(
        userId: String,
        habitId: String
    ): Result<Unit>

    suspend fun markHabitCompleted(
        userId: String,
        habitId: String,
        date: String
    ): Result<Unit>
}
