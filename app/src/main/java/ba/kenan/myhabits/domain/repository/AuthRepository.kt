package ba.kenan.myhabits.domain.repository

import java.util.Date

interface AuthRepository {
    suspend fun registerUser(
        email: String,
        password: String,
        name: String,
        birthDate: Date
    ): Result<Unit>

    suspend fun loginUser(
        email: String,
        password: String
    ): Result<Unit>
}
