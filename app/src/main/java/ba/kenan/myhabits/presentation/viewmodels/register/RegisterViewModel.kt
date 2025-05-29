package ba.kenan.myhabits.presentation.viewmodels.register

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date
import java.util.TimeZone

class RegisterViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Init)
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun registerUser(email: String, password: String, name: String, birthDate: Date) {
        _uiState.value = RegisterUiState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                saveUserProfile(uid, email, name, birthDate)
            }
            .addOnFailureListener { e ->
                _uiState.value = RegisterUiState.Failure(e)
            }
    }

    private fun saveUserProfile(uid: String, email: String, name: String, birthDate: Date) {
        val user = mapOf(
            "name" to name,
            "email" to email,
            "birthDate" to Timestamp(birthDate),
            "timezone" to TimeZone.getDefault().id,
            "joinedOn" to Timestamp(Date()),
            "habits" to emptyList<String>()
        )

        firestore.collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                _uiState.value = RegisterUiState.Success
            }
            .addOnFailureListener {
                _uiState.value = RegisterUiState.Failure(it)
            }
    }
}
