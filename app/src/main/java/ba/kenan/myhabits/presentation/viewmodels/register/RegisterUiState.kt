package ba.kenan.myhabits.presentation.viewmodels.register

sealed class RegisterUiState {
    data object Init : RegisterUiState()
    data object Loading : RegisterUiState()
    data object Success : RegisterUiState()
    data class Failure(val error: Throwable) : RegisterUiState()
}
