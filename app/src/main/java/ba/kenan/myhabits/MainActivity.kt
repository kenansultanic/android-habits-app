package ba.kenan.myhabits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ba.kenan.myhabits.presentation.ui.screens.MainScreen
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyHabitsAppTheme {
                MainScreen()
            }
        }
    }
}
