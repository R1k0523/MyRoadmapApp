package ru.boringowl.myroadmapapp.presentation.features.todos.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.presentation.base.rememberForeverLazyListState
import ru.boringowl.myroadmapapp.presentation.base.resetScroll


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun TodosScreen(
    navController: NavController,
    viewModel: TodosViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = { TodosAppBar(viewModel)}
    ) { p ->
        val todos = viewModel.modelList.collectAsState(listOf()).value
        Column(
            modifier = Modifier
                .padding(p)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            if (viewModel.filteredIsEmpty()) {
                Text(
                    text = stringResource(id = R.string.nothing_found),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center

                )
            }
            LazyColumn(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = rememberForeverLazyListState(stringResource(R.string.nav_routes)),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(todos.sortedBy { it.header }) { t ->
                    AnimatedVisibility(viewModel.isFiltered(t)) {
                        TodoView(navController, t, viewModel)
                    }
                }
            }
        }
    }

    LaunchedEffect(true) {
        viewModel.fetchTodos()
    }
}


@Composable
fun TodosAppBar(viewModel: TodosViewModel) {
    SmallTopAppBar(title = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp, 8.dp)
        ) {
            if (viewModel.isSearchOpened)
                OutlinedTextField(
                    value = viewModel.searchText,
                    onValueChange = { viewModel.searchText = it },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    textStyle = MaterialTheme.typography.bodyMedium,
                )
            else {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.nav_todos),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            val tag = stringResource(R.string.nav_todos)
            IconButton(modifier = Modifier.size(28.dp),
                onClick = {
                    viewModel.isSearchOpened = !viewModel.isSearchOpened
                    viewModel.searchText = ""
                    resetScroll(tag)
                }) {
                if (viewModel.isSearchOpened)
                    Icon(
                        Icons.Rounded.Close,
                        "Закрыть",
                        tint = MaterialTheme.colorScheme.primary
                    )
                else
                    Icon(
                        Icons.Rounded.Search,
                        "Найти",
                        tint = MaterialTheme.colorScheme.primary
                    )
            }
        }
    })
}