package ba.kenan.myhabits.presentation.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ba.kenan.myhabits.R
import ba.kenan.myhabits.domain.model.Frequency
import ba.kenan.myhabits.domain.model.Habit
import ba.kenan.myhabits.presentation.ui.components.LoadingComponent
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.utils.DevicesPreview
import ba.kenan.myhabits.presentation.viewmodels.home.HomeUiState
import ba.kenan.myhabits.presentation.viewmodels.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import java.sql.Timestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAddNewHabitClick: () -> Unit,
    onUpdateHabitClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var showArchived by remember { mutableStateOf(true) }
    var showCompleted by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        viewModel.loadHabits()
    }

    when (uiState) {
        is HomeUiState.Loading -> LoadingComponent()

        is HomeUiState.Error -> {
            Log.e("Home", "Failed to load habits", (uiState as HomeUiState.Error).error)
        }

        is HomeUiState.Success, HomeUiState.Init -> {
            val allHabits = (uiState as? HomeUiState.Success)?.habits.orEmpty()
            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

            val visibleHabits = allHabits
                .filter { showArchived || !it.isArchived }
                .filter { showCompleted || it.history[today] != true }

            HomeScreen(
                habits = visibleHabits,
                showArchived = showArchived,
                setShowArchived = { showArchived = it },
                showCompleted = showCompleted,
                setShowCompleted = { showCompleted = it },
                onAddNewHabitClick = onAddNewHabitClick,
                onUpdate = { habit -> onUpdateHabitClick(habit.id) },
                onDelete = { habit -> viewModel.deleteHabit(habit.id) },
                onArchive = { habit -> viewModel.updateHabit(habit.copy(isArchived = !habit.isArchived)) },
                onMarkAsCompleted = { habitId -> viewModel.markHabitAsCompleted(habitId) },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun HomeScreen(
    habits: List<Habit>,
    showArchived: Boolean,
    setShowArchived: (Boolean) -> Unit,
    showCompleted: Boolean,
    setShowCompleted: (Boolean) -> Unit,
    onAddNewHabitClick: () -> Unit,
    onUpdate: (Habit) -> Unit,
    onDelete: (Habit) -> Unit,
    onArchive: (Habit) -> Unit,
    onMarkAsCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewHabitClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_habit)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter toggles
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Show archived",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = showArchived,
                    onCheckedChange = setShowArchived
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Show completed",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = showCompleted,
                    onCheckedChange = setShowCompleted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = habits,
                    key = { habit -> habit.id + habit.history.toString() }
                ) { habit ->
                    HabitCard(
                        habit = habit,
                        onUpdate = onUpdate,
                        onDelete = onDelete,
                        onArchive = onArchive,
                        onMarkAsCompleted = onMarkAsCompleted
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HabitCard(
    habit: Habit,
    onUpdate: (Habit) -> Unit,
    onDelete: (Habit) -> Unit,
    onArchive: (Habit) -> Unit,
    onMarkAsCompleted: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    val visibleTags = if (expanded) habit.tags else habit.tags.take(2)
    val hasHiddenTags = habit.tags.size > visibleTags.size
    val today = LocalDate.now()
    val dayOfWeekIndex = today.dayOfWeek.value % 7
    val isActiveToday = habit.frequency.days.contains(dayOfWeekIndex)
    val dateString = today.format(DateTimeFormatter.ISO_DATE)
    val alreadyCompleted = habit.history[dateString] == true

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text =
                            if (habit.isArchived)
                                "Archived"
                            else if (alreadyCompleted || !isActiveToday) "Completed"
                            else "Not completed",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }
                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                onUpdate(habit)
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (habit.isArchived) "Unarchive" else "Archive") },
                            onClick = {
                                onArchive(habit)
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                onDelete(habit)
                                menuExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                visibleTags.forEach { tag ->
                    AssistChip(
                        onClick = {},
                        label = { Text(tag) }
                    )
                }
                if (hasHiddenTags && !expanded) {
                    AssistChip(
                        onClick = { expanded = true },
                        label = { Text("...") }
                    )
                }
                if ( isActiveToday && !alreadyCompleted && !habit.isArchived) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onMarkAsCompleted(habit.id) },
                        modifier = Modifier
                    ) {
                        Text("Mark as done")
                    }
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = "Detalji navike (placeholder)",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }
            }
        }
    }
}

@DevicesPreview
@Composable
private fun HomeScreenPreview() {
    MyHabitsAppTheme {
        HomeScreen(
            habits = listOf(
                Habit(
                    name = "Running",
                    tags = listOf("health", "fitness"),
                    isArchived = false,
                    createdAt = Timestamp(Date().time),
                    frequency = Frequency(
                        type = "Weekly",
                        days = listOf(1, 3, 5)
                    )
                ),
                Habit(
                    name = "Reading",
                    tags = listOf("personal growth", "education"),
                    isArchived = false,
                    createdAt = Timestamp(Date().time),
                    frequency = Frequency(
                        type = "Daily",
                        days = emptyList()
                    )
                ),
                Habit(
                    name = "Meditation",
                    tags = listOf("mindfulness", "mental health"),
                    isArchived = false,
                    createdAt = Timestamp(Date().time),
                    frequency = Frequency(
                        type = "Custom",
                        days = listOf(0, 6)
                    )
                ),
                Habit(
                    name = "Learning Kotlin",
                    tags = listOf("programming", "career", "learning"),
                    isArchived = false,
                    createdAt = Timestamp(Date().time),
                    frequency = Frequency(
                        type = "Weekly",
                        days = listOf(2, 4)
                    )
                )
            ),
            showArchived = true,
            showCompleted = true,
            setShowArchived = { },
            setShowCompleted = { },
            onAddNewHabitClick = { },
            onDelete = { },
            onArchive = { },
            onUpdate = { },
            onMarkAsCompleted = { }
        )
    }
}
