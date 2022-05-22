package ru.boringowl.myroadmapapp.presentation.features._root

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.boringowl.myroadmapapp.presentation.features.auth.AccountViewModel
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationItem

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootScreen(
    accountViewModel: AccountViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val startDestination = when (accountViewModel.currentUser.collectAsState().value) {
        null -> NavigationItem.Login
        else -> NavigationItem.Hackathons
    }
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        NavigationItem.values().forEach { item ->
            composable(item.route) {
                item.screen.invoke(navController)
            }
        }
    }

}