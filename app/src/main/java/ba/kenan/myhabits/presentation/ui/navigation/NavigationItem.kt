package ba.kenan.myhabits.presentation.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    @StringRes val titleId: Int,
    val icon: ImageVector,
    val route: String
)
