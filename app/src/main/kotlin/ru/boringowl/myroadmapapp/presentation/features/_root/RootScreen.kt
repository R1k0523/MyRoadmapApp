package ru.boringowl.myroadmapapp.presentation.features._root

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.boringowl.myroadmapapp.presentation.base.BottomNavigationBar
import ru.boringowl.myroadmapapp.presentation.features.auth.AccountViewModel
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationItem

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RootScreen(
    accountViewModel: AccountViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val startDestination = when (accountViewModel.currentUser.collectAsState().value) {
        null -> NavigationItem.Login
        else -> NavigationItem.Todo
    }

    Scaffold(
        bottomBar = {
            if (startDestination != NavigationItem.Login)
                BottomNavigationBar(navController = navController)
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination.route,
            modifier = Modifier.padding(it)
        ) {
            NavigationItem.values().forEach { item ->
                composable(item.route) {
                    item.screen.invoke(navController)
                }
            }
        }
    }
}