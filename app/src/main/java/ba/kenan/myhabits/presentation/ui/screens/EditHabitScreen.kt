package ba.kenan.myhabits.presentation.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ba.kenan.myhabits.presentation.ui.components.LoadingComponent
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.utils.DevicesPreview
import ba.kenan.myhabits.presentation.utils.HabitFormData
import ba.kenan.myhabits.presentation.viewmodels.home.HomeUiState
import ba.kenan.myhabits.presentation.viewmodels.home.HomeViewModel
import ba.kenan.myhabits.domain.model.Habit
import ba.kenan.myhabits.presentation.ui.components.HabitForm
import com.google.firebase.Timestamp

@Composable
fun EditHabitScreen(
    viewModel: HomeViewModel,
    habitId: String,
    onHabitEdited: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(habitId) {
        viewModel.loadHabits()
    }

    when (uiState) {
        is HomeUiState.Loading -> LoadingComponent()
        is HomeUiState.Success -> {
            val habit = (uiState as HomeUiState.Success).habits.find { it.id == habitId }
            if (habit != null) {
                EditHabitContent(
                    habit = habit,
                    onHabitEdited = {
                        viewModel.updateHabit(
                            habit.copy(
                                name = it.name,
                                tags = it.tags,
                                frequency = habit.frequency.copy(
                                    type = it.frequencyType,
                                    days = it.frequencyDays
                                )
                            )
                        )
                        onHabitEdited()
                    },
                    modifier = modifier
                )
            } else {
                Log.e("EditHabit", "Habit not found with id: $habitId")
            }
        }
        is HomeUiState.Error -> {
            Log.e("EditHabit", "Greška pri dohvatanju navike", (uiState as HomeUiState.Error).error)
        }
        HomeUiState.Init -> LoadingComponent()
    }
}

@Composable
private fun EditHabitContent(
    habit: Habit,
    onHabitEdited: (HabitFormData) -> Unit,
    modifier: Modifier = Modifier
) {
    HabitForm(
        initialName = habit.name,
        initialTags = habit.tags,
        initialFrequencyType = habit.frequency.type,
        initialFrequencyDays = habit.frequency.days,
        onSubmit = { name, tags, freqType, days ->
            onHabitEdited(HabitFormData(name, tags, freqType, days))
        },
        modifier = modifier
    )
}

@DevicesPreview
@Composable
private fun EditHabitScreenPreview() {
    MyHabitsAppTheme {
        val dummyHabit = Habit(
            id = "1",
            name = "Reading",
            tags = listOf("education", "focus"),
            isArchived = false,
            createdAt = Timestamp.now().toDate(),
            frequency = ba.kenan.myhabits.domain.model.Frequency(
                type = "Weekly",
                days = listOf(1, 3, 5)
            )
        )
        EditHabitContent(habit = dummyHabit, onHabitEdited = {})
    }
}
