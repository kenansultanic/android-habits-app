package ba.kenan.myhabits.presentation.viewmodels.login

sealed class LoginUiState {
    data object Init : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class Failure(val error: Throwable) : LoginUiState()
}
