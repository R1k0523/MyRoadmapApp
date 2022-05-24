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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.boringowl.myroadmapapp.presentation.base.BottomNavigationBar
import ru.boringowl.myroadmapapp.presentation.features.auth.AccountViewModel
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationItem
import ru.boringowl.myroadmapapp.presentation.features.auth.resetpassword.ResetPasswordScreen
import ru.boringowl.myroadmapapp.presentation.features.auth.signin.SignInScreen
import ru.boringowl.myroadmapapp.presentation.features.auth.signup.SignUpScreen
import ru.boringowl.myroadmapapp.presentation.features.hackathons.HackathonsScreen
import ru.boringowl.myroadmapapp.presentation.features.routes.RoutesScreen
import ru.boringowl.myroadmapapp.presentation.features.profile.ProfileScreen
import ru.boringowl.myroadmapapp.presentation.features.skills.SkillsScreen

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
            composable(NavigationItem.Profile.route) {
                ProfileScreen(navController)
            }
            composable(NavigationItem.Hackathons.route) {
                HackathonsScreen(navController)
            }
            composable(NavigationItem.Todo.route) {
            }
            composable(NavigationItem.TodoDetails.route) {
            }
            composable(NavigationItem.Skills.route,
                arguments = listOf(navArgument("routeId") { type = NavType.IntType })
            ) {
                val routeId = it.arguments?.getInt("routeId")
                if (routeId != null)
                    SkillsScreen(navController, routeId)
                else
                    navController.navigateUp()
            }
            composable(NavigationItem.Route.route) {
                RoutesScreen(navController)
            }
            composable(NavigationItem.Login.route) {
                SignInScreen(navController)
            }
            composable(NavigationItem.ForgotPassword.route) {
                ResetPasswordScreen(navController)
            }
            composable(NavigationItem.Register.route) {
                SignUpScreen(navController)
            }
        }
    }
}