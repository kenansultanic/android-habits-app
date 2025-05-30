package ba.kenan.myhabits.domain.repository

import ba.kenan.myhabits.domain.models.UserProfile

interface ProfileRepository {
    suspend fun getUserProfile(userId: String): Result<UserProfile>
}
