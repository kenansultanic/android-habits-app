package ba.kenan.myhabits.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.ui.theme.fontBlack
import ba.kenan.myhabits.presentation.ui.theme.fontDarkGrey
import ba.kenan.myhabits.presentation.ui.theme.fontGrey

@Composable
fun InfoCard(
    title: String,
    items: List<Pair<String, String>>
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 28.dp, top = 28.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 24.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            items.forEachIndexed { index, (label, value) ->
                InfoItem(label, value)
                if (index < items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 0.7.dp,
                        color = fontGrey.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = fontGrey,
                fontSize = 20.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoCardPreview() {
    MyHabitsAppTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            InfoCard(
                title = "Information",
                items = listOf(
                    "Name" to "Kenan Sultanić",
                    "Email" to "kenan@gmail.com",
                    "Timezone" to "Europe/Sarajevo",
                    "Age" to "24",
                    "Joined" to "29/05/2025"
                )
            )
        }
    }
}
