package ru.boringowl.myroadmapapp.presentation.base

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationInfo

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = NavigationInfo.bottomBarItems
    NavigationBar {
        var selectedItem by remember { mutableStateOf(0) }
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.route
                    )
                },
                label = { Text(stringResource(id = item.title)) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route)
                },
                alwaysShowLabel = true
            )
        }
    }
}