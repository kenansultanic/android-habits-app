package ba.kenan.myhabits.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ba.kenan.myhabits.R
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.utils.DropdownItem

@Composable
fun HabitForm(
    onSubmit: (name: String, tags: List<String>, freqType: String, days: List<Int>) -> Unit,
    modifier: Modifier = Modifier,
    initialName: String = "",
    initialTags: List<String> = emptyList(),
    initialFrequencyType: String = "",
    initialFrequencyDays: List<Int> = emptyList()
) {
    var name by remember { mutableStateOf(TextFieldValue(initialName)) }
    var tags by remember { mutableStateOf(TextFieldValue(initialTags.joinToString(", "))) }
    var frequencyType by remember { mutableStateOf(initialFrequencyType) }
    var frequencyDays by remember { mutableStateOf(initialFrequencyDays.map { it.toString() }) }

    val frequencyTypeOptions = listOf(
        DropdownItem("daily", "Daily"),
        DropdownItem("weekly", "Weekly"),
        DropdownItem("custom", "Custom")
    )

    val dayOptions = listOf(
        DropdownItem("0", "Sunday"),
        DropdownItem("1", "Monday"),
        DropdownItem("2", "Tuesday"),
        DropdownItem("3", "Wednesday"),
        DropdownItem("4", "Thursday"),
        DropdownItem("5", "Friday"),
        DropdownItem("6", "Saturday")
    )

    LaunchedEffect(frequencyType) {
        frequencyDays = when (frequencyType) {
            "daily" -> (0..6).map { it.toString() }
            "weekly" -> listOf()
            "custom" -> frequencyDays
            else -> listOf()
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 40.dp),
    ) {
        Column {
            CustomTextField(
                text = name,
                placeholder = stringResource(R.string.habit_name_placeholder),
                onTextValueChange = { name = it },
                modifier = Modifier.padding(vertical = 4.dp)
            )
            CustomTextField(
                text = tags,
                placeholder = stringResource(R.string.habit_tags_placeholder),
                onTextValueChange = { tags = it },
                modifier = Modifier.padding(vertical = 4.dp)
            )
            CustomDropdownMenu(
                placeholder = stringResource(R.string.select_frequency_type),
                items = frequencyTypeOptions,
                selectedItemValue = frequencyTypeOptions.find { it.id == frequencyType }?.name,
                selectItemId = { frequencyType = it },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )

            when (frequencyType) {
                "weekly" -> {
                    CustomDropdownMenu(
                        placeholder = stringResource(R.string.select_day),
                        items = dayOptions,
                        selectedItemValue = dayOptions.find { it.id == frequencyDays.firstOrNull() }?.name,
                        selectItemId = { frequencyDays = listOf(it) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                }

                "custom" -> {
                    CustomMultiSelectDropdownMenu(
                        placeholder = stringResource(R.string.select_days),
                        items = dayOptions,
                        selectedIds = frequencyDays,
                        onSelectionChange = { frequencyDays = it },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                }

                "daily" -> {
                    // no-op
                }
            }
        }

        CustomButton(
            text = stringResource(R.string.save_button),
            onClick = {
                val tagList = tags.text.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                val dayList = frequencyDays.mapNotNull { it.toIntOrNull() }
                onSubmit(name.text.trim(), tagList, frequencyType, dayList)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HabitFormScreenPreview() {
    MyHabitsAppTheme {
        HabitForm(onSubmit = { a, b, c, d ->})
    }
}
