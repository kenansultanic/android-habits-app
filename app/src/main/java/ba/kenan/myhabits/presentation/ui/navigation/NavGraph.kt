package ba.kenan.myhabits.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ba.kenan.myhabits.presentation.ui.screens.AboutScreen
import ba.kenan.myhabits.presentation.ui.screens.AddHabitScreen
import ba.kenan.myhabits.presentation.ui.screens.EditHabitScreen
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
            LoginScreen(
                viewModel = hiltViewModel(),
                onLoginButtonClick = {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                navigateToRegistration = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(
                viewModel = hiltViewModel(),
                onRegisterButtonClick = {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                navigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Profile.route) {
            if (!isLoggedIn) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                }
            } else {
                ProfileScreen(viewModel = hiltViewModel())
            }
        }

        composable(route = Screen.Home.route) {
            if (!isLoggedIn) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            } else {
                HomeScreen(
                    viewModel = hiltViewModel(),
                    onAddNewHabitClick = { navController.navigate(Screen.AddHabit.route) },
                    onUpdateHabitClick = { habitId ->
                        navController.navigate(Screen.EditHabit.createRoute(habitId))
                    }
                )
            }
        }

        composable(route = Screen.Settings.route) {
            if (!isLoggedIn) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route)
                }
            } else {
                SettingsScreen()
            }
        }

        composable(route = Screen.About.route) {
            if (!isLoggedIn) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.About.route)
                }
            } else {
                AboutScreen()
            }
        }

        composable(Screen.AddHabit.route) {
            if (!isLoggedIn) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Settings.route) { inclusive = true }
                    }
                }
            } else {
                AddHabitScreen(
                    viewModel = hiltViewModel(),
                    onHabitAdded = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = Screen.EditHabit.route,
            arguments = listOf(navArgument("habitId") { type = NavType.StringType })
        ) { backStackEntry ->
            if (!isLoggedIn) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Settings.route) { inclusive = true }
                    }
                }
            } else {
                val habitId = backStackEntry.arguments?.getString("habitId") ?: ""
                EditHabitScreen(
                    viewModel = hiltViewModel(),
                    habitId = habitId,
                    onHabitEdited = { navController.popBackStack() }
                )
            }
        }
    }
}
