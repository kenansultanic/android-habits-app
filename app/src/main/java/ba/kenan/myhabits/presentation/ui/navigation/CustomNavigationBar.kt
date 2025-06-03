package ba.kenan.myhabits.presentation.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme

@Composable
fun CustomNavigationBar(
    items: List<NavigationItem>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    HorizontalDivider(
        thickness = 0.3.dp,
        color = Color.LightGray
    )
    Spacer(modifier = Modifier.height(16.dp))
    NavigationBar(
        containerColor = Color.Transparent
    ) {
        items.forEachIndexed { index, item ->
            val selected = selectedItemIndex == index

            NavigationBarItem(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(bottom = 4.dp),
                selected = selected,
                onClick = { onItemSelected(index) },
                label = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = item.titleId),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (selected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(top = 8.dp)
                                    .height(4.dp)
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .fillMaxWidth(1f)
                            )
                        }
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = stringResource(id = item.titleId))
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomNavigationBarPreview() {
    MyHabitsAppTheme {
        CustomNavigationBar(
            items = NavigationItems.items,
            selectedItemIndex = 1,
            onItemSelected = { }
        )
    }
}
