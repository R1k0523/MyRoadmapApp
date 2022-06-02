package ru.boringowl.myroadmapapp.presentation.features.routes

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.model.Route
import ru.boringowl.myroadmapapp.presentation.base.rememberForeverLazyListState
import ru.boringowl.myroadmapapp.presentation.base.resetScroll
import ru.boringowl.myroadmapapp.presentation.navigation.NavigationItem


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun RoutesScreen(
    navController: NavController,
    viewModel: RoutesViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState, snackbar = { Snackbar(snackbarData = it) }) },
        topBar = {
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
                            text = stringResource(R.string.nav_routes),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    val tag = stringResource(R.string.nav_routes)
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
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { p ->
        val routes = viewModel.modelList.collectAsState(listOf()).value
        Column(
            modifier = Modifier
                .padding(p)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            if (viewModel.filteredIsEmpty()) {
                Text(
                    "Ничего не найдено",
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
                items(routes.sortedBy { it.index() }) { r ->
                    AnimatedVisibility(viewModel.isFiltered(r)) {
                        RouteView(navController, viewModel, r)
                    }
                }
            }
        }
    }
    if (viewModel.isDialogOpened)
        CreateDialog(viewModel, navController, snackbarHostState)
    LaunchedEffect(true) {
        viewModel.fetch()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteView(
    navController: NavController,
    viewModel: RoutesViewModel,
    r: Route
) {
    val isTrendingIcon = if (r.index() < 3)
        Icons.Rounded.TrendingUp
    else if (r.index() in 3..6)
        Icons.Rounded.TrendingFlat
    else
        Icons.Rounded.TrendingDown
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { navController.navigate("skills/${r.routeId}")
            }
    ) {
        Column {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        r.routeName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(0.8f)
                    )
                    Icon(
                        isTrendingIcon,
                        "Состояние",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Вакансии: ${r.vacanciesCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Резюме: ${r.resumesCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    r.routeDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            viewModel.pickedRoute = r.routeId!!
                            viewModel.routeName = r.routeName
                            viewModel.isDialogOpened = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, end = 4.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) { Text("Создать план") }
                    OutlinedButton(
                        onClick = { navController.navigate("books/${r.routeId}") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, start = 4.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) { Text("Книги") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDialog(
    viewModel: RoutesViewModel,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    Dialog(
        onDismissRequest = { viewModel.isDialogOpened = false },
        content = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                ElevatedCard() {
                    Column(Modifier.padding(24.dp, 16.dp)) {
                        Text(
                            text = "Создание плана",
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Normal,
                        )
                        OutlinedTextField(
                            value = viewModel.todoName,
                            onValueChange = { viewModel.todoName = it },
                            singleLine = true,
                            label = { Text("Название плана") },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyMedium,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Будет создан ваш личный список с навыками, " +
                                "указанными в направлении, где Вы сможете " +
                                "отмечать свой уровень знаний.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.add(
                                    onError = {
                                        viewModel.isDialogOpened = false
                                        snackbarHostState.showSnackbar(it)
                                    },
                                    onSuccess = {
                                        viewModel.isDialogOpened = false
                                        snackbarHostState.showSnackbar("Список создан")
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text(stringResource(R.string.save)) }
                        OutlinedButton(
                            onClick = {
                                viewModel.isDialogOpened = false
                                viewModel.todoName = ""
                                viewModel.routeName = ""
                                viewModel.pickedRoute = -1
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text(stringResource(R.string.cancel)) }

                    }
                }
            }
        }
    )
}
