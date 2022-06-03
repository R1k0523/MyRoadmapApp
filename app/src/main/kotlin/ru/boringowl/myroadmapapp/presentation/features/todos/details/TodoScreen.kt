package ru.boringowl.myroadmapapp.presentation.features.todos.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.boringowl.myroadmapapp.R
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
    val listState = rememberLazyListState()

    Scaffold(
        topBar = { TodoTopBar(viewModel) { listState.scrollToItem(index = 0) } }
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

@Composable
fun TodoTopBar(viewModel: TodoViewModel, scrollToTop: suspend () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
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
                        imageVector = Icons.Rounded.Sort,
                        contentDescription = stringResource(R.string.filter),
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
                                    scrollToTop()
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