package ba.kenan.myhabits.presentation.viewmodels.home

import ba.kenan.myhabits.domain.model.Habit

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data object Init : HomeUiState()
    data class Success(val habits: List<Habit>) : HomeUiState()
    data class Error(val error: Throwable) : HomeUiState()
}
