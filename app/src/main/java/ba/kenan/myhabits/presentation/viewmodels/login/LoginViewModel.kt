package ba.kenan.myhabits.presentation.viewmodels.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Init)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun loginUser(email: String, password: String) {
        _uiState.value = LoginUiState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _uiState.value = LoginUiState.Success
            }
            .addOnFailureListener { e ->
                _uiState.value = LoginUiState.Failure(e)
            }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Init
    }
}
