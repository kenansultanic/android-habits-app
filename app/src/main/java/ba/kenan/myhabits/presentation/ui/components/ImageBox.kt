package ba.kenan.myhabits.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.ui.theme.circleBorder
import ba.kenan.myhabits.presentation.ui.theme.profileBackground

@Composable
fun ImageBox(
    initials: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 28.sp)
) {
    Box(
        modifier = modifier
            .size(80.dp)
            .clip(CircleShape)
            .border(1.dp, circleBorder, CircleShape)
            .background(profileBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = textStyle
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageSelectorNoPreview() {
    MyHabitsAppTheme {
        ImageBox(
            initials = "KS"
        )
    }
}
