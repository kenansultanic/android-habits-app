package ba.kenan.myhabits.presentation.ui.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ba.kenan.myhabits.R
import ba.kenan.myhabits.domain.model.Frequency
import ba.kenan.myhabits.domain.model.Habit
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.utils.DevicesPreview
import java.sql.Timestamp
import java.util.Date

@Composable
fun HomeScreen(
    habits: List<Habit>,
    onUpdate: (Habit) -> Unit,
    onDelete: (Habit) -> Unit,
    onArchive: (Habit) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_habit))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(habits) { habit ->
                HabitCard(
                    habit = habit,
                    onUpdate = onUpdate,
                    onDelete = onDelete,
                    onArchive = onArchive
                )
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
    onArchive: (Habit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = habit.createdAt.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
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
                habit.tags.forEach { tag ->
                    AssistChip(
                        onClick = {  },
                        label = { Text(tag) }
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = "Edit Tags (placeholder)",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
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
                    tags = listOf("programming", "career"),
                    isArchived = false,
                    createdAt = Timestamp(Date().time),
                    frequency = Frequency(
                        type = "Weekly",
                        days = listOf(2, 4)
                    )
                )
            ),
            onDelete = { habit -> },
            onArchive = { habit -> },
            onUpdate = { habit -> }
        )
    }
}
