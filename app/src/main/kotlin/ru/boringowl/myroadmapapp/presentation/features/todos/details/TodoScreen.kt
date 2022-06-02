package ru.boringowl.myroadmapapp.presentation.features.todos.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.model.SkillTodo
import java.util.*


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalTextApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun TodoScreen(
    navController: NavController,
    todoId: UUID,
    viewModel: TodoViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            SmallTopAppBar(title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp, 8.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.nav_todos),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box {
                        Row(
                            Modifier.clickable { viewModel.filterOpened = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                viewModel.sortTypes.displayName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Light
                            )
                            Icon(
                                Icons.Rounded.Sort,
                                "Фильтр",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = viewModel.filterOpened,
                            onDismissRequest = { viewModel.filterOpened = false },
                        ) {
                            SortTodosBy.values().forEachIndexed { index, s ->
                                DropdownMenuItem(
                                    onClick = {
                                        coroutineScope.launch {
                                            viewModel.sortTypes = s
                                            viewModel.filterOpened = false
                                            listState.scrollToItem(index = 0)
                                        }
                                    },
                                    text = { Text(s.displayName) }
                                )
                            }
                        }
                    }
                }
            })
        }
    ) { p ->
        val todo = viewModel.model.collectAsState(null).value
        Column(
            modifier = Modifier
                .padding(p)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.Center
        ) {
            todo?.skills?.let { skills ->
                LazyColumn(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = listState,
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(viewModel.sort(skills), key = { it.skillName }) { st ->
                        SkillTodoView(st, viewModel)
                    }
                }
            }
        }
    }
    LaunchedEffect(true) {
        viewModel.fetchTodoSkills(todoId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillTodoView(t: SkillTodo, viewModel: TodoViewModel) {
    var full by remember { mutableStateOf(false) }
    var favorite by remember { mutableStateOf(t.favorite) }
    var text by remember { mutableStateOf(t.notes) }
    var progress by remember { mutableStateOf(t.progress) }
    var name by remember { mutableStateOf(t.manualName) }
    val focuser = LocalFocusManager.current
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clickable {
                    full = !full
                    focuser.clearFocus()
                }
                .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(modifier = Modifier.size(28.dp),
                    onClick = {
                        favorite = !favorite
                        viewModel.update(t.apply { this.favorite = favorite })
                    }
                ) {
                    Icon(
                        Icons.Rounded.Star,
                        "Фильтр",
                        tint = if (favorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                            0.2f
                        )
                    )
                }
                Spacer(Modifier.width(8.dp))
                Column {
                    Box {
                        BasicTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                if (it.isNotEmpty())
                                    viewModel.update(t.apply { this.manualName = name })
                            },
                            textStyle = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                        )
                        if (name.isEmpty())
                            Text(
                                "Название",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                    }
                    Text(
                        "Важность: ${t.necessity}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
                Spacer(Modifier.weight(1f))
                Icon(
                    if (full) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    "Закрыть",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(30.dp)
                )
            }
            if (full) {
                Spacer(Modifier.height(8.dp))
                if (t.manualName.lowercase() != t.skillName.lowercase())
                    Text(
                        "Навык: ${t.skillName}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                Spacer(Modifier.height(8.dp))
                ProgressDropdownMenu(progress) {
                    progress = it
                    viewModel.update(t.apply { this.progress = it })
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        viewModel.update(t.apply { this.notes = text })
                    },
                    label = { Text(stringResource(R.string.notes)) },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun ProgressDropdownMenu(initial: Int, onChoose: (value: Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(
        "Не знаю",
        "Базовое знакомство",
        "Использовал в проектах",
        "Знаю продвинутые вещи",
        "Пользуюсь без интернета",
        "Знаю, как работает внутри",
    )
    var selectedIndex by remember { mutableStateOf(initial) }
    val focuser = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
    ) {
        OutlinedTextField(
            value = items[selectedIndex],
            onValueChange = {},
            label = { Text("Уровень") },
            singleLine = false,
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    expanded = it.hasFocus
                },
            trailingIcon = {
                Icon(
                    if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    "",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        )
        DropdownMenu(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.80f),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                focuser.clearFocus()
            },
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expanded = false
                        focuser.clearFocus()
                        onChoose(selectedIndex)
                    },
                    text = { Text(s) }
                )
            }
        }
    }
}
