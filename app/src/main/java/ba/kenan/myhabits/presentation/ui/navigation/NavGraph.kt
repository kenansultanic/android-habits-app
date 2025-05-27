package ba.kenan.myhabits.presentation.ui.navigation

import android.app.Activity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ba.kenan.myhabits.presentation.ui.screens.HomeScreen
import ba.kenan.myhabits.presentation.ui.screens.LoginScreen
import ba.kenan.myhabits.presentation.ui.screens.ProfileScreen
import ba.kenan.myhabits.presentation.ui.screens.RegisterScreen
import ba.kenan.myhabits.presentation.ui.screens.SettingsScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Profile.route else Screen.Login.route,
        modifier = modifier
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen()
        }

        composable(route = Screen.Register.route) {
            RegisterScreen()
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen()
        }

        composable(route = Screen.Home.route) {
            HomeScreen()
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
