package ba.kenan.myhabits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ba.kenan.myhabits.presentation.ui.navigation.NavGraph
import ba.kenan.myhabits.presentation.ui.navigation.NavigationBar
import ba.kenan.myhabits.presentation.ui.navigation.NavigationItems
import ba.kenan.myhabits.presentation.ui.navigation.Screen
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            var selectedItemIndex by rememberSaveable { mutableIntStateOf(1) }

            val auth = FirebaseAuth.getInstance()
            var firebaseUser by remember { mutableStateOf(auth.currentUser) }

            DisposableEffect(Unit) {
                val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                    firebaseUser = firebaseAuth.currentUser
                }
                auth.addAuthStateListener(listener)
                onDispose {
                    auth.removeAuthStateListener(listener)
                }
            }

            val isLoggedIn = firebaseUser != null

            MyHabitsAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
                        val showBottomBar = currentDestination !in listOf(Screen.Login.route, Screen.Register.route)

                        if (showBottomBar) {
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
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        isLoggedIn = isLoggedIn,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
