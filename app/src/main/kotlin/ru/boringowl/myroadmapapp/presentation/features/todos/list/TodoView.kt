package ru.boringowl.myroadmapapp.presentation.features.todos.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.model.Todo
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoView(
    navController: NavController,
    t: Todo,
    viewModel: TodosViewModel
) {
    var isMenuOpened by remember { mutableStateOf(false) }
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable {
                navController.navigate("todoDetails/${t.todoId}") {
                    popUpTo(NavigationItem.Todo.route)
                }
            }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            var progress = t.skills?.sumOf { it.progress } ?: 0
            var all = t.skills?.size?.times(5) ?: 0
            if (t.skills.isNullOrEmpty()) {
                progress = t.ready
                all = t.full
            }
            Column(Modifier.weight(1f)) {
                Text(
                    t.header,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.CheckBox,
                        "Отмечено",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "$progress/$all",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Light,
                    )
                }
            }
            Box {
                Icon(
                    Icons.Rounded.MoreVert,
                    stringResource(R.string.more),
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.clickable { isMenuOpened = true }
                )
                DropdownMenu(
                    expanded = isMenuOpened,
                    onDismissRequest = { isMenuOpened = false },
                    modifier = Modifier.height(40.dp)
                ) {
                    DropdownMenuItem(
                        modifier = Modifier.height(40.dp),
                        onClick = {
                            viewModel.delete(t)
                            isMenuOpened = false
                        },
                        text = { Text(stringResource(R.string.delete), Modifier.height(40.dp)) }
                    )
                }
            }
        }
    }
}
