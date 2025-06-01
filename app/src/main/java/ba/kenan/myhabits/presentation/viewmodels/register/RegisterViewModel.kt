package ba.kenan.myhabits.presentation.viewmodels.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.kenan.myhabits.domain.repository.AuthRepository
import ba.kenan.myhabits.presentation.utils.SnackbarController
import ba.kenan.myhabits.presentation.utils.SnackbarEvent
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Init)
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun registerUser(email: String, password: String, name: String, birthDate: Date) {
        _uiState.value = RegisterUiState.Loading

        viewModelScope.launch {
            val result = authRepository.registerUser(email, password, name, birthDate)

            _uiState.value = result.fold(
                onSuccess = { RegisterUiState.Success },
                onFailure = {
                    snackbarController.sendEvent(
                        SnackbarEvent(message = it.message ?: "Unknown error")
                    )
                    RegisterUiState.Failure(it)
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Init
    }
}
