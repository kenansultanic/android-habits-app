package ba.kenan.myhabits.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import ba.kenan.myhabits.R
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.ui.theme.paleGrey
import ba.kenan.myhabits.presentation.ui.theme.textFieldFocused
import ba.kenan.myhabits.presentation.utils.DropdownItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(
    placeholder: String,
    items: List<DropdownItem>,
    selectItemId: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    selectedItemValue: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItemName by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        if (!title.isNullOrEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            val borderColor = if (expanded) textFieldFocused else paleGrey
            Row(modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_xsmall))) {
                OutlinedTextField(
                    value = selectedItemName.ifEmpty { selectedItemValue ?: placeholder },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = stringResource(R.string.pick_item_from_dropdown_content_description),
                            tint = paleGrey
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = textFieldFocused,
                        unfocusedTextColor = paleGrey,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .border(
                            dimensionResource(R.dimen.border_thin),
                            borderColor,
                            OutlinedTextFieldDefaults.shape
                        )
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .requiredSizeIn(maxHeight = dimensionResource(R.dimen.height_xx_large))
                    .exposedDropdownSize()
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        onClick = {
                            selectedItemName = item.name
                            selectItemId(item.id)
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors(textColor = paleGrey),
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CustomDropdownMenuPreview() {
    MyHabitsAppTheme {
        CustomDropdownMenu(
            title = "Item Title",
            placeholder = "Choose Item",
            items = listOf(
                DropdownItem(id = "1", name = "Item A"),
                DropdownItem(id = "2", name = "Item B"),
                DropdownItem(id = "3", name = "Item C")
            ),
            selectItemId = {},
        )
    }
}
