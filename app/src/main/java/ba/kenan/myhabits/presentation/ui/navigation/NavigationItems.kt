package ba.kenan.myhabits.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import ba.kenan.myhabits.R

object NavigationItems {
    val items = listOf(
        NavigationItem(
            titleId = R.string.home,
            icon = Icons.Default.Home,
            route = Screen.Home.route
        ),
        NavigationItem(
            titleId = R.string.profile,
            icon = Icons.Default.Person,
            route = Screen.Profile.route
        ),
        NavigationItem(
            titleId = R.string.home,
            icon = Icons.Default.Search,
            route = Screen.Register.route
        ),
        NavigationItem(
            titleId = R.string.settings,
            icon = Icons.Default.Settings,
            route = Screen.Settings.route
        )
    )
}
