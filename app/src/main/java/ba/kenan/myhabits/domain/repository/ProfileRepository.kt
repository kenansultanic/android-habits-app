package ba.kenan.myhabits.domain.repository

import ba.kenan.myhabits.domain.model.UserProfile
import com.google.firebase.firestore.Source

interface ProfileRepository {
    suspend fun getUserProfile(userId: String, source: Source): Result<UserProfile>
}
