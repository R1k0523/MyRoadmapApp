package ru.boringowl.myroadmapapp.presentation.features._root

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import ru.boringowl.myroadmapapp.presentation.base.BottomNavigationBar
import ru.boringowl.myroadmapapp.presentation.base.ConnectivityStatus
import ru.boringowl.myroadmapapp.presentation.features.auth.AccountViewModel
import ru.boringowl.myroadmapapp.presentation.features.auth.resetpassword.ResetPasswordScreen
import ru.boringowl.myroadmapapp.presentation.features.auth.signin.SignInScreen
import ru.boringowl.myroadmapapp.presentation.features.auth.signup.SignUpScreen
import ru.boringowl.myroadmapapp.presentation.features.books.BooksScreen
import ru.boringowl.myroadmapapp.presentation.features.hackathons.HackathonsScreen
import ru.boringowl.myroadmapapp.presentation.features.profile.ProfileScreen
import ru.boringowl.myroadmapapp.presentation.features.routes.RoutesScreen
import ru.boringowl.myroadmapapp.presentation.features.skills.SkillsScreen
import ru.boringowl.myroadmapapp.presentation.features.todos.details.TodoScreen
import ru.boringowl.myroadmapapp.presentation.features.todos.list.TodosScreen
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationItem
import java.util.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RootScreen(
    viewModel: AccountViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    var animationDisplayed by remember { mutableStateOf(true) }
    val user = viewModel.currentUser.collectAsState().value

    LaunchedEffect(true){
        viewModel.fetchUser()
        delay(3000)
        animationDisplayed = false
    }
    val startDestination = when {
        animationDisplayed -> NavigationItem.Splash
        user == null -> NavigationItem.Login
        else -> NavigationItem.Todo
    }

    var (selectedItem, select) = remember { mutableStateOf(0) }
    if (user == null)
        select(0)
    Scaffold(
        bottomBar = {
            if (!animationDisplayed) {
                Column {
                    ConnectivityStatus()
                    if (startDestination != NavigationItem.Login)
                        BottomNavigationBar(navController, selectedItem, select)
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination.route,
            modifier = Modifier.padding(it)
        ) {
            composable(NavigationItem.Splash.route) {
                Splash()
            }
            composable(NavigationItem.Profile.route) {
                ProfileScreen(navController)
            }
            composable(NavigationItem.Hackathons.route) {
                HackathonsScreen(navController)
            }
            composable(NavigationItem.Todo.route) {
                TodosScreen(navController)
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
            composable(NavigationItem.TodoDetails.route,
                arguments = listOf(navArgument("todoId") { type = NavType.StringType })
            ) { entry ->
                val todoId = entry.arguments?.getString("todoId")
                todoId?.let { TodoScreen(navController, UUID.fromString(todoId)) }
                if (todoId == null) navController.navigateUp()
            }
            composable(NavigationItem.Skills.route,
                arguments = listOf(navArgument("routeId") { type = NavType.IntType })
            ) { entry ->
                val routeId = entry.arguments?.getInt("routeId")
                routeId?.let { SkillsScreen(navController, routeId) }
                if (routeId == null) navController.navigateUp()
            }
            composable(NavigationItem.Books.route,
                arguments = listOf(navArgument("routeId") { type = NavType.IntType })
            ) { entry ->
                val routeId = entry.arguments?.getInt("routeId")
                routeId?.let { BooksScreen(navController, routeId) }
                if (routeId == null) navController.navigateUp()
            }
        }
    }

}



