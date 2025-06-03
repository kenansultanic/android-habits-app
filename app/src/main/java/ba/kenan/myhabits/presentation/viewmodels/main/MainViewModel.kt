package ba.kenan.myhabits.presentation.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.kenan.myhabits.presentation.utils.SnackbarController
import ba.kenan.myhabits.presentation.utils.SnackbarEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _firebaseUser = MutableStateFlow(auth.currentUser)
    val firebaseUser: StateFlow<FirebaseUser?> = _firebaseUser.asStateFlow()

    private val _selectedItemIndex = MutableStateFlow(1)
    val selectedItemIndex: StateFlow<Int> = _selectedItemIndex.asStateFlow()

    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _firebaseUser.value = firebaseAuth.currentUser
    }

    init {
        auth.addAuthStateListener(authListener)
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authListener)
    }

    fun onTabSelected(index: Int) {
        _selectedItemIndex.value = index
    }

    fun logout() {
        auth.signOut()
        _firebaseUser.value = null
    }

    fun getSnackbarEvents() = snackbarController.events

    fun onMenuClick() {
        viewModelScope.launch {
            snackbarController.sendEvent(
                SnackbarEvent(message = "This menu is currently non-functional.")
            )
        }
    }
}
