package ba.kenan.myhabits.presentation.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen(route = "login_screen")
    data object Register : Screen(route = "register_screen")
    data object Home : Screen(route = "home_screen")
    data object Profile : Screen(route = "profile_screen")
    data object About : Screen(route = "about_screen")
    data object Settings : Screen(route = "settings_screen")
    data object AddHabit : Screen(route = "add_habit")
    data object EditHabit : Screen(route = "edit_habit/{habitId}") {
        fun createRoute(habitId: String) = "edit_habit/$habitId"
    }
}
