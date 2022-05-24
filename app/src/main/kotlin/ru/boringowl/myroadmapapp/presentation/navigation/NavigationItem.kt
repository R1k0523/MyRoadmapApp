package ru.boringowl.myroadmapapp.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import ru.boringowl.myroadmapapp.R

enum class NavigationItem(
    var route: String,
    @StringRes var title: Int,
    var icon: ImageVector,
) {
    Profile("profile", R.string.nav_profile, Icons.Rounded.Person),
    Hackathons("hackathons", R.string.nav_hackathon, Icons.Rounded.SafetyDivider),
    Todo("todo", R.string.nav_todos, Icons.Rounded.Inventory),
    TodoDetails("todoDetails", R.string.nav_todo, Icons.Rounded.ViewList),
    Skills("skills/{routeId}", R.string.nav_skills, Icons.Rounded.Code),
    Route("route", R.string.nav_routes, Icons.Rounded.CallSplit),
    Login("auth", R.string.nav_login, Icons.Rounded.Login),
    ForgotPassword("forgot", R.string.nav_forgot_password, Icons.Rounded.Password),
    Register("register", R.string.nav_register, Icons.Rounded.PersonAdd),
}

object NavigationInfo {
    val bottomBarItems = arrayListOf(
        NavigationItem.Todo,
        NavigationItem.Route,
        NavigationItem.Hackathons,
        NavigationItem.Profile
    )

}