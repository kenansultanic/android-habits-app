package ba.kenan.myhabits.presentation.viewmodels.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.kenan.myhabits.domain.model.Habit
import ba.kenan.myhabits.domain.repository.ProfileRepository
import ba.kenan.myhabits.presentation.utils.SnackbarController
import ba.kenan.myhabits.presentation.utils.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            val result = repository.getUserProfile(userId)
            _uiState.value = result.fold(
                onSuccess = { ProfileUiState.Success(it) },
                onFailure = {
                    snackbarController.sendEvent(
                        SnackbarEvent(message = it.message ?: "Unknown error")
                    )
                    ProfileUiState.Error(it)
                }
            )
        }
    }

    fun shouldShowMotivation(habits: List<Habit>, streakFailLimit: Int = 3): Boolean {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ISO_DATE

        return habits.any { habit ->
            val created = habit.createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            if (ChronoUnit.DAYS.between(created, today) < streakFailLimit) return@any false

            val sortedHistory = habit.history
                .mapNotNull { (dateStr, completed) ->
                    runCatching { LocalDate.parse(dateStr, formatter) to completed }.getOrNull()
                }
                .sortedByDescending { it.first }

            val recentFailures = sortedHistory
                .filter { (date, _) ->
                    val dayIndex = date.dayOfWeek.value % 7
                    when (habit.frequency.type.lowercase()) {
                        "daily" -> true
                        "weekly" -> habit.frequency.days.firstOrNull() == dayIndex
                        "custom" -> habit.frequency.days.contains(dayIndex)
                        else -> false
                    }
                }
                .take(streakFailLimit)
                .all { (_, completed) -> !completed }

            recentFailures
        }
    }
}
