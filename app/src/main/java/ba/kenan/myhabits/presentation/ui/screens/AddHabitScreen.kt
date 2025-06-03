package ba.kenan.myhabits.presentation.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ba.kenan.myhabits.presentation.ui.components.HabitForm
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.utils.DevicesPreview
import ba.kenan.myhabits.presentation.utils.HabitFormData
import ba.kenan.myhabits.presentation.viewmodels.home.HomeUiState
import ba.kenan.myhabits.presentation.viewmodels.home.HomeViewModel

@Composable
fun AddHabitScreen(
    viewModel: HomeViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is HomeUiState.Error -> {
            Log.e("AddHabit", "Greška pri dodavanju navike", (uiState as HomeUiState.Error).error)
            navigateBack()
        }
        else -> {
            AddHabitScreen(onHabitAdded = {
                viewModel.addHabit(it.name, it.tags, it.frequencyType, it.frequencyDays)
                navigateBack()
            }, modifier = modifier)
        }
    }
}

@Composable
private fun AddHabitScreen(
    onHabitAdded: (HabitFormData) -> Unit,
    modifier: Modifier = Modifier
) {
    HabitForm(
        onSubmit = { name, tags, freqType, days ->
            onHabitAdded(HabitFormData(name, tags, freqType, days))
        },
        modifier = modifier
    )
}

@DevicesPreview
@Composable
private fun AddHabitScreenPreview() {
    MyHabitsAppTheme {
        AddHabitScreen(onHabitAdded = {})
    }
}
