package ba.kenan.myhabits.presentation.viewmodels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.kenan.myhabits.domain.model.Habit
import ba.kenan.myhabits.domain.network.NetworkStatusProvider
import ba.kenan.myhabits.domain.repository.HabitRepository
import ba.kenan.myhabits.presentation.utils.SnackbarController
import ba.kenan.myhabits.presentation.utils.SnackbarEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HabitRepository,
    private val snackbarController: SnackbarController,
    private val networkStatusProvider: NetworkStatusProvider
) : ViewModel() {

    private val source = if (networkStatusProvider.isOffline()) Source.CACHE else Source.DEFAULT
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadHabits() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val result = repository.getHabitsForUser(uid, source)
            _uiState.value = result.fold(
                onSuccess = { HomeUiState.Success(it) },
                onFailure = {
                    if (it !is CancellationException) {
                        snackbarController.sendEvent(
                            SnackbarEvent(message = it.message ?: "Unknown error")
                        )
                    }
                    HomeUiState.Error(it)
                }
            )
        }
    }

    fun addHabit(
        name: String,
        tags: List<String>,
        frequencyType: String,
        frequencyDays: List<Int>
    ) {
        viewModelScope.launch {
            if (networkStatusProvider.isOffline()) {
                snackbarController.sendEvent(
                    SnackbarEvent("Can't add habits without an internet connection")
                )
                _uiState.value = HomeUiState.Error(Throwable(message = "No internet connection"))
                return@launch
            }
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val result = repository.addHabit(uid, name, tags, frequencyType, frequencyDays)
            result.fold(
                onSuccess = {
                    loadHabits()
                    snackbarController.sendEvent(
                        SnackbarEvent(message = "New habit added.")
                    )
                },
                onFailure = {
                    snackbarController.sendEvent(
                        SnackbarEvent(it.message ?: "Couldn't add habit.")
                    )
                    _uiState.value = HomeUiState.Error(it)
                }
            )
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            if (networkStatusProvider.isOffline()) {
                snackbarController.sendEvent(
                    SnackbarEvent("Can't update habits without an internet connection")
                )
                return@launch
            }
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val result = repository.updateHabit(
                userId = uid,
                habitId = habit.id,
                name = habit.name,
                tags = habit.tags,
                frequencyType = habit.frequency.type,
                frequencyDays = habit.frequency.days,
                isArchived = habit.isArchived
            )
            result.fold(
                onSuccess = {
                    loadHabits()
                    snackbarController.sendEvent(
                        SnackbarEvent(message = "Habit updated.")
                    )
                },
                onFailure = {
                    snackbarController.sendEvent(
                        SnackbarEvent(it.message ?: "Couldn't update habit.")
                    )
                    _uiState.value = HomeUiState.Error(it)
                }
            )
        }
    }

    fun deleteHabit(habitId: String) {
        viewModelScope.launch {
            if (networkStatusProvider.isOffline()) {
                snackbarController.sendEvent(
                    SnackbarEvent("Can't delete habits without an internet connection")
                )
                return@launch
            }
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val result = repository.deleteHabit(uid, habitId)
            result.fold(
                onSuccess = {
                    loadHabits()
                    snackbarController.sendEvent(
                        SnackbarEvent(message = "Habit deleted.")
                    )
                },
                onFailure = {
                    snackbarController.sendEvent(
                        SnackbarEvent(it.message ?: "Couldn't delete habit.")
                    )
                    HomeUiState.Error(it)
                }
            )
        }
    }

    fun markHabitAsCompleted(habitId: String) {
        viewModelScope.launch {
            if (networkStatusProvider.isOffline()) {
                snackbarController.sendEvent(
                    SnackbarEvent("Can't complete habits without an internet connection")
                )
                return@launch
            }
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val date = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            repository.markHabitCompleted(userId, habitId, date)
            val result = repository.getHabitsForUser(userId, source)
            _uiState.value = result.fold(
                onSuccess = {
                    snackbarController.sendEvent(
                        SnackbarEvent(message = "Habit completed")
                    )
                    HomeUiState.Success(it.toList())
                },
                onFailure = {
                    snackbarController.sendEvent(
                        SnackbarEvent(message = it.message ?: "Unknown error")
                    )
                    HomeUiState.Error(it)
                }
            )
        }
    }
}
