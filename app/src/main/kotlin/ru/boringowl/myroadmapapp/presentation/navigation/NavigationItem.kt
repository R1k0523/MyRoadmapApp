package ru.boringowl.myroadmapapp.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.presentation.view.auth.LoginScreen
import ru.boringowl.myroadmapapp.presentation.view.hackathons.HackathonsScreen

enum class NavigationItem(
    var route: String,
    @StringRes var title: Int,
    var icon: ImageVector,
    var screen: @Composable (navController: NavController) -> Unit,
) {
    Profile("profile", R.string.nav_profile, Icons.Rounded.Person, {}),
    Settings("settings", R.string.nav_settings, Icons.Rounded.Reorder, {}),
    Hackathons("hackathons", R.string.nav_hackathon, Icons.Rounded.SafetyDivider, {
        HackathonsScreen(it)
    }),
    Todo("todo", R.string.nav_todos, Icons.Rounded.Inventory, {}),
    TodoDetails("todoDetails", R.string.nav_todo, Icons.Rounded.ViewList, {}),
    Community("community", R.string.community, Icons.Rounded.Groups, {}),
    Skills("skills", R.string.nav_skills, Icons.Rounded.Code, {}),
    Route("route", R.string.nav_routes, Icons.Rounded.CallSplit, {}),
    Login("auth", R.string.nav_login, Icons.Rounded.Login, {
        LoginScreen(it)
    }),
    ForgotPassword("forgot", R.string.nav_forgot_password, Icons.Rounded.Password, {}),
    Register("register", R.string.nav_register, Icons.Rounded.PersonAdd, {}),
}

object NavigationInfo {
    val bottomBarItems = arrayListOf(
        NavigationItem.Todo,
        NavigationItem.Route,
        NavigationItem.Community,
        NavigationItem.Profile
    )

}