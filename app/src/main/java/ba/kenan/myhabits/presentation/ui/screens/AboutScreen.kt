package ba.kenan.myhabits.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.utils.DevicesPreview

@Composable
fun AboutScreen(){
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "About Screen",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@DevicesPreview
@Composable
private fun AboutScreenPreview() {
    MyHabitsAppTheme {
        AboutScreen()
    }
}
