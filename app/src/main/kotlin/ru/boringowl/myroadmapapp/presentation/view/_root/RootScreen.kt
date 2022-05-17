package ru.boringowl.myroadmapapp.presentation.view._root

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationItem
import ru.boringowl.myroadmapapp.presentation.viewmodel.*

@Composable
fun RootScreen(
    accountViewModel: AccountViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val startDestination = when (accountViewModel.currentUser) {
        null -> NavigationItem.Login
        else -> NavigationItem.Todo
    }
    NavHost(navController = navController, startDestination = startDestination.route) {
        NavigationItem.values().forEach { item ->
            composable(item.route) {
                item.screen.invoke(navController)
            }
        }
    }
}