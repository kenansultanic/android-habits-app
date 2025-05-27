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
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NavigationBar(
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
                            color = if (selected) Color.Black else Color.LightGray,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (selected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(top = 8.dp)
                                    .height(4.dp)
                                    .background(Color.Yellow)
                                    .fillMaxWidth(1f)
                            )
                        }
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = stringResource(id = item.titleId))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
