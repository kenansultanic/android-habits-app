package ba.kenan.myhabits.data.repository

import ba.kenan.myhabits.data.utils.toHabit
import ba.kenan.myhabits.domain.model.Habit
import ba.kenan.myhabits.domain.repository.HabitRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class HabitRepositoryImpl : HabitRepository {
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getHabitsForUser(userId: String): Result<List<Habit>> {
        return try {
            val snapshot = firestore
                .collection("users")
                .document(userId)
                .collection("habits")
                .get()
                .await()

            val habits = snapshot.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null
                val habit = data.toHabit(doc.id)

                val historySnapshot = firestore
                    .collection("users")
                    .document(userId)
                    .collection("habits")
                    .document(doc.id)
                    .collection("history")
                    .get()
                    .await()

                val historyMap = historySnapshot.documents.associate { historyDoc ->
                    val date = historyDoc.id
                    val completed = historyDoc.getBoolean("completed") ?: false
                    date to completed
                }

                habit.copy(history = historyMap)
            }

            Result.success(habits)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addHabit(
        userId: String,
        name: String,
        tags: List<String>,
        frequencyType: String,
        frequencyDays: List<Int>
    ): Result<Unit> {
        return try {
            val habitData = mapOf(
                "name" to name,
                "tags" to tags,
                "frequency" to mapOf("type" to frequencyType, "days" to frequencyDays),
                "createdAt" to Timestamp.now(),
                "isArchived" to false,
                "userId" to userId
            )

            firestore
                .collection("users")
                .document(userId)
                .collection("habits")
                .add(habitData)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateHabit(
        userId: String,
        habitId: String,
        name: String,
        tags: List<String>,
        frequencyType: String,
        frequencyDays: List<Int>,
        isArchived: Boolean
    ): Result<Unit> {
        return try {
            val habitRef = firestore
                .collection("users")
                .document(userId)
                .collection("habits")
                .document(habitId)

            val data = mapOf(
                "name" to name,
                "tags" to tags,
                "frequency" to mapOf("type" to frequencyType, "days" to frequencyDays),
                "isArchived" to isArchived
            )

            habitRef.set(data, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteHabit(userId: String, habitId: String): Result<Unit> = try {
        firestore
            .collection("users")
            .document(userId)
            .collection("habits")
            .document(habitId)
            .delete()
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun markHabitCompleted(userId: String, habitId: String, date: String): Result<Unit> {
        return try {
            val historyRef = firestore
                .collection("users")
                .document(userId)
                .collection("habits")
                .document(habitId)
                .collection("history")
                .document(date)

            historyRef.set(mapOf("completed" to true), SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
