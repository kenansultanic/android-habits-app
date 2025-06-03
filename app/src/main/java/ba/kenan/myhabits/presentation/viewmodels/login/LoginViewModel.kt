package ba.kenan.myhabits.presentation.viewmodels.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.kenan.myhabits.domain.repository.AuthRepository
import ba.kenan.myhabits.presentation.utils.SnackbarController
import ba.kenan.myhabits.presentation.utils.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val authRepository: AuthRepository,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Init)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun loginUser(email: String, password: String) {
        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = authRepository.loginUser(email, password)
            _uiState.value = result.fold(
                onSuccess = { LoginUiState.Success },
                onFailure = {
                    snackbarController.sendEvent(
                        SnackbarEvent(message = it.message ?: "Unknown error")
                    )
                    LoginUiState.Failure(it)
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Init
    }
}
