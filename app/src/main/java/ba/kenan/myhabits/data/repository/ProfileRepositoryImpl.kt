package ba.kenan.myhabits.data.repository

import ba.kenan.myhabits.domain.model.Frequency
import ba.kenan.myhabits.domain.model.Habit
import ba.kenan.myhabits.domain.model.UserProfile
import ba.kenan.myhabits.domain.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import java.util.Date

class ProfileRepositoryImpl : ProfileRepository {
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getUserProfile(userId: String, source: Source): Result<UserProfile> = try {
        val userSnapshot = firestore.collection("users").document(userId).get().await()

        val name = userSnapshot.getString("name") ?: ""
        val email = userSnapshot.getString("email") ?: ""
        val birthDate = userSnapshot.getTimestamp("birthDate")?.toDate() ?: Date()
        val timezone = userSnapshot.getString("timezone") ?: ""
        val joinedOn = userSnapshot.getTimestamp("joinedOn")?.toDate() ?: Date()

        val habitsSnapshot = firestore
            .collection("users")
            .document(userId)
            .collection("habits")
            .get(source)
            .await()

        val habits = habitsSnapshot.documents.mapNotNull { doc ->
            val data = doc.data ?: return@mapNotNull null
            val id = doc.id

            val fullName = data["name"] as? String ?: ""
            val tags = data["tags"] as? List<String> ?: emptyList()
            val isArchived = data["isArchived"] as? Boolean ?: false
            val createdAt = (data["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date()

            val freqMap = data["frequency"] as? Map<*, *>
            val frequency = Frequency(
                type = freqMap?.get("type") as? String ?: "",
                days = (freqMap?.get("days") as? List<Long>)?.map { it.toInt() } ?: emptyList()
            )

            val historySnapshot = firestore
                .collection("users")
                .document(userId)
                .collection("habits")
                .document(id)
                .collection("history")
                .get(source)
                .await()

            val historyMap = historySnapshot.documents.associate { historyDoc ->
                val date = historyDoc.id
                val completed = historyDoc.getBoolean("completed") ?: false
                date to completed
            }

            Habit(id, fullName, tags, frequency, isArchived, createdAt, historyMap)
        }

        Result.success(UserProfile(name, email, birthDate, timezone, joinedOn, habits))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
