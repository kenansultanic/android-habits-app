package ba.kenan.myhabits.data.repository

import ba.kenan.myhabits.domain.model.Frequency
import ba.kenan.myhabits.domain.model.Habit
import ba.kenan.myhabits.domain.model.UserProfile
import ba.kenan.myhabits.domain.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class ProfileRepositoryImpl : ProfileRepository {
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getUserProfile(userId: String): Result<UserProfile> = try {
        val userSnapshot = firestore.collection("users").document(userId).get().await()

        val name = userSnapshot.getString("name") ?: ""
        val email = userSnapshot.getString("email") ?: ""
        val birthDate = userSnapshot.getTimestamp("birthDate")?.toDate() ?: Date()
        val timezone = userSnapshot.getString("timezone") ?: ""
        val joinedOn = userSnapshot.getTimestamp("joinedOn")?.toDate() ?: Date()

        val habitsSnapshot = firestore.collection("habits")
            .whereEqualTo("userId", userId).get().await()

        val habits = habitsSnapshot.documents.map { doc ->
            val id = doc.id
            val fullName = doc.getString("name") ?: ""
            val tags = doc.get("tags") as? List<String> ?: emptyList()
            val isArchived = doc.getBoolean("isArchived") ?: false
            val createdAt = doc.getTimestamp("createdAt")?.toDate() ?: Date()
            val freqMap = doc.get("frequency") as? Map<*, *>
            val frequency = Frequency(
                type = freqMap?.get("type") as? String ?: "",
                days = (freqMap?.get("days") as? List<Long>)?.map { it.toInt() } ?: emptyList()
            )

            val historyMap = mutableMapOf<String, Boolean>()
            val historySnapshot = firestore.collection("habits")
                .document(id).collection("history").get().await()
            for (historyDoc in historySnapshot.documents) {
                val completed = historyDoc.getBoolean("completed") ?: false
                historyMap[historyDoc.id] = completed
            }

            Habit(id, fullName, tags, frequency, isArchived, createdAt, historyMap)
        }

        Result.success(UserProfile(name, email, birthDate, timezone, joinedOn, habits))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
