package ba.kenan.myhabits.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ba.kenan.myhabits.R
import ba.kenan.myhabits.presentation.ui.components.CustomSnackbar
import ba.kenan.myhabits.presentation.ui.navigation.NavGraph
import ba.kenan.myhabits.presentation.ui.navigation.CustomNavigationBar
import ba.kenan.myhabits.presentation.ui.navigation.NavigationItems
import ba.kenan.myhabits.presentation.ui.navigation.Screen
import ba.kenan.myhabits.presentation.utils.ObserveAsEvents
import ba.kenan.myhabits.presentation.viewmodels.main.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    var menuExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val firebaseUser by viewModel.firebaseUser.collectAsState()
    val selectedItemIndex by viewModel.selectedItemIndex.collectAsState()

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    val showTopBar = currentDestination !in listOf(Screen.Login.route, Screen.Register.route)
    val showBottomBar = showTopBar
    val isLoggedIn = firebaseUser != null

    ObserveAsEvents(flow = viewModel.getSnackbarEvents(), snackbarHostState) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                event.action?.onClick?.invoke()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_vertical_medium)),
                snackbar = { snackbarData ->
                    CustomSnackbar(snackbarData)
                }
            )
        },
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.myhabits),
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.onMenuClick() }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More options")
                            }
                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Logout") },
                                    onClick = {
                                        viewModel.logout()
                                        menuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                CustomNavigationBar(
                    items = NavigationItems.items,
                    selectedItemIndex = selectedItemIndex,
                    onItemSelected = { index ->
                        viewModel.onTabSelected(index)
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
