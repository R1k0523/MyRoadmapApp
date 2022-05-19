package ru.boringowl.myroadmapapp.presentation.view.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavController
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationInfo
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationItem
import java.security.AccessController.getContext

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = NavigationInfo.bottomBarItems
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.surface,
    ) {
        var selectedItem by remember { mutableStateOf(0) }
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    BadgedBox(badge = {
                        Badge { Text(index.toString()) }
                    })
                    {
                        Icon(
                            item.icon,
                            contentDescription = item.route
                        )
                    }
                },
                label = { stringResource(id = item.title) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route)
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.surface),
                alwaysShowLabel = true
            )
        }
    }
}