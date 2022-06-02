package ru.boringowl.myroadmapapp.presentation.features._root

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.presentation.base.BottomNavigationBar
import ru.boringowl.myroadmapapp.presentation.base.ConnectivityStatus
import ru.boringowl.myroadmapapp.presentation.features.auth.AccountViewModel
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationItem
import ru.boringowl.myroadmapapp.presentation.features.auth.resetpassword.ResetPasswordScreen
import ru.boringowl.myroadmapapp.presentation.features.auth.signin.SignInScreen
import ru.boringowl.myroadmapapp.presentation.features.auth.signup.SignUpScreen
import ru.boringowl.myroadmapapp.presentation.features.hackathons.HackathonsScreen
import ru.boringowl.myroadmapapp.presentation.features.routes.RoutesScreen
import ru.boringowl.myroadmapapp.presentation.features.profile.ProfileScreen
import ru.boringowl.myroadmapapp.presentation.features.books.BooksScreen
import ru.boringowl.myroadmapapp.presentation.features.skills.SkillsScreen
import ru.boringowl.myroadmapapp.presentation.features.todos.list.TodosScreen
import ru.boringowl.myroadmapapp.presentation.features.todos.details.TodoScreen
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
                select(NavigationItem.Splash.sector)
                Splash()
            }
            composable(NavigationItem.Profile.route) {
                select(NavigationItem.Profile.sector)
                ProfileScreen(navController)
            }
            composable(NavigationItem.Hackathons.route) {
                select(NavigationItem.Hackathons.sector)
                HackathonsScreen(navController)
            }
            composable(NavigationItem.Todo.route) {
                select(NavigationItem.Todo.sector)
                TodosScreen(navController)
            }
            composable(NavigationItem.TodoDetails.route,
                arguments = listOf(navArgument("todoId") { type = NavType.StringType })
            ) {
                select(NavigationItem.TodoDetails.sector)
                val todoId = it.arguments?.getString("todoId")
                if (todoId != null)
                    TodoScreen(navController, UUID.fromString(todoId))
                else
                    navController.navigateUp()
            }
            composable(NavigationItem.Skills.route,
                arguments = listOf(navArgument("routeId") { type = NavType.IntType })
            ) {
                select(NavigationItem.Skills.sector)
                val routeId = it.arguments?.getInt("routeId")
                if (routeId != null)
                    SkillsScreen(navController, routeId)
                else
                    navController.navigateUp()
            }
            composable(NavigationItem.Route.route) {
                select(NavigationItem.Route.sector)
                RoutesScreen(navController)
            }
            composable(NavigationItem.Books.route,
                arguments = listOf(navArgument("routeId") { type = NavType.IntType })
            ) {
                select(NavigationItem.Books.sector)
                val routeId = it.arguments?.getInt("routeId")
                if (routeId != null)
                    BooksScreen(navController, routeId)
                else
                    navController.navigateUp()
            }
            composable(NavigationItem.Login.route) {
                select(NavigationItem.Login.sector)
                SignInScreen(navController)
            }
            composable(NavigationItem.ForgotPassword.route) {
                select(NavigationItem.ForgotPassword.sector)
                ResetPasswordScreen(navController)
            }
            composable(NavigationItem.Register.route) {
                select(NavigationItem.Register.sector)
                SignUpScreen(navController)
            }
        }
    }

}



@Composable
fun Splash() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_myroadmap),
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxSize(0.8f)
                .align(Alignment.Center)
        )
    }
}