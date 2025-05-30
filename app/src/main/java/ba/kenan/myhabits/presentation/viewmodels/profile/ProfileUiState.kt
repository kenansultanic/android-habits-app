package ba.kenan.myhabits.presentation.viewmodels.profile

import ba.kenan.myhabits.domain.models.UserProfile

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Success(val profile: UserProfile) : ProfileUiState()
    data class Error(val error: Throwable) : ProfileUiState()
}
