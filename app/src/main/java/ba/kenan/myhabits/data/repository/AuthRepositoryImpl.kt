package ba.kenan.myhabits.data.repository

import ba.kenan.myhabits.domain.repository.AuthRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.TimeZone

class AuthRepositoryImpl : AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun registerUser(
        email: String,
        password: String,
        name: String,
        birthDate: Date
    ): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("UID is null"))

            val user = mapOf(
                "name" to name,
                "email" to email,
                "birthDate" to Timestamp(birthDate),
                "timezone" to TimeZone.getDefault().id,
                "joinedOn" to Timestamp(Date()),
                "habits" to emptyList<String>()
            )

            firestore.collection("users")
                .document(uid)
                .set(user)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginUser(
        email: String,
        password: String
    ): Result<Unit> = try {
        auth.signInWithEmailAndPassword(email, password).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
