package ba.kenan.myhabits.presentation.ui.components

import android.text.format.DateUtils
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.tooling.preview.Preview
import ba.kenan.myhabits.R
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.ui.theme.lightGrey
import ba.kenan.myhabits.presentation.ui.theme.textFieldUnfocused
import ba.kenan.myhabits.presentation.utils.convertMillisToDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    selectedDateMillis: Long?,
    textFieldPlaceholder: String,
    setSelectedDate: (Long) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis,)

    val selectedDate = datePickerState.selectedDateMillis?.let {
        setSelectedDate(it)
        DateUtils().convertMillisToDate(it)
    } ?: ""

    Column(modifier = modifier) {
        if (!title.isNullOrEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = Medium)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = dimensionResource(id = R.dimen.border_thin),
                    color = textFieldUnfocused,
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.rounded_corner_small))
                )
                .clickable { showDatePicker = !showDatePicker },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(R.string.calendar_icon),
                tint = lightGrey,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_default),
                    vertical = dimensionResource(id = R.dimen.padding_default)
                )
            )
            Text(
                text = selectedDate.ifEmpty { textFieldPlaceholder },
                style = MaterialTheme.typography.headlineMedium.copy(color = lightGrey)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.date_pick_arrow),
                tint = lightGrey,
                modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_default))
            )
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text(
                        text = stringResource(R.string.ok),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color.Black,
                            fontWeight = Medium
                        )
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                colors = DatePickerDefaults.colors()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomDatePickerPreview() {
    MyHabitsAppTheme {
        CustomDatePicker(
            selectedDateMillis = null,
            title = "Date of Birth",
            textFieldPlaceholder = "Choose Date of Birth",
            setSelectedDate = { }
        )
    }
}
