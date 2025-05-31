package ba.kenan.myhabits.presentation.viewmodels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.kenan.myhabits.domain.model.Habit
import ba.kenan.myhabits.domain.repository.HabitRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadHabits() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val result = repository.getHabitsForUser(uid)
            _uiState.value = result.fold(
                onSuccess = { HomeUiState.Success(it) },
                onFailure = { HomeUiState.Error(it) }
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
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            repository.addHabit(uid, name, tags, frequencyType, frequencyDays)
            loadHabits()
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            repository.updateHabit(
                userId = userId,
                habitId = habit.id,
                name = habit.name,
                tags = habit.tags,
                frequencyType = habit.frequency.type,
                frequencyDays = habit.frequency.days,
                isArchived = habit.isArchived
            )
            loadHabits()
        }
    }

    fun deleteHabit(habitId: String) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            repository.deleteHabit(userId, habitId)
            loadHabits()
        }
    }

    fun markHabitAsCompleted(habitId: String) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val date = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

            repository.markHabitCompleted(userId, habitId, date)

            val result = repository.getHabitsForUser(userId)
            _uiState.value = result.fold(
                onSuccess = { HomeUiState.Success(it.toList()) },
                onFailure = { HomeUiState.Error(it) }
            )
        }
    }
}
