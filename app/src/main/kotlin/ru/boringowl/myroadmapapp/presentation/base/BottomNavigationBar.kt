package ru.boringowl.myroadmapapp.presentation.base

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationInfo
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationItem

@Composable
fun BottomNavigationBar(navController: NavController, selected: Int, select: (i: Int) -> Unit) {
    val items = NavigationInfo.bottomBarItems
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.route
                    )
                },
                label = { Text(stringResource(id = item.title)) },
                selected = selected == index,
                onClick = {
                    if (navController.currentDestination?.route != items[index].route) {
                        select(index)
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = selected != index
                        }
                    }
                },
                alwaysShowLabel = true
            )
        }
    }
}