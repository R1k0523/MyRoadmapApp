package ru.boringowl.myroadmapapp.presentation.base

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationInfo

@Composable
fun BottomNavigationBar(navController: NavController, selected: Int, select: (i: Int) -> Unit) {
    val items = NavigationInfo.bottomBarItems
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.route
                    )
                },
                label = { Text(stringResource(item.title)) },
                onClick = {
                    if (navController.currentDestination?.route != items[index].route) {
                        select(index)
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                            restoreState = selected != index
                        }
                    }
                },
                selected = selected == index,
                alwaysShowLabel = true
            )
        }
    }
}