package ba.kenan.myhabits.domain.repository

import ba.kenan.myhabits.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getUserProfile(userId: String): Result<UserProfile>
}
