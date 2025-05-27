package ba.kenan.myhabits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import ba.kenan.myhabits.presentation.ui.navigation.NavGraph
import ba.kenan.myhabits.presentation.ui.navigation.NavigationBar
import ba.kenan.myhabits.presentation.ui.navigation.NavigationItems
import ba.kenan.myhabits.presentation.ui.navigation.Screen
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
            val navController = rememberNavController()

            MyHabitsAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            items = NavigationItems.items,
                            selectedItemIndex = selectedItemIndex,
                            onItemSelected = { index ->
                                selectedItemIndex = index
                                navController.navigate(NavigationItems.items[index].route) {
                                    launchSingleTop = true
                                    popUpTo(Screen.Profile.route)
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        isLoggedIn = true,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
