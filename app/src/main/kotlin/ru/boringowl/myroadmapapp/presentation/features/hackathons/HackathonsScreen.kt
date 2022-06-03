package ru.boringowl.myroadmapapp.presentation.features.hackathons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.launch
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.presentation.base.rememberForeverLazyListState
import ru.boringowl.myroadmapapp.presentation.base.resetScroll


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun HackathonsScreen(
    navController: NavController,
    viewModel: HackathonViewModel = hiltViewModel(),
) {
    val state = rememberForeverLazyListState(stringResource(R.string.nav_hackathon))
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = { HackathonTopBar(viewModel) },
        floatingActionButton = {
            if (state.firstVisibleItemIndex != 0) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                    coroutineScope.launch {
                        state.scrollToItem(0)
                    }
                }) {
                    Icon(
                        Icons.Rounded.ArrowUpward,
                        "Вверх",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
    ) { p ->
        val hacks = viewModel.modelList.collectAsLazyPagingItems()
        LazyColumn(
            Modifier
                .fillMaxSize().padding(p),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = state,
            contentPadding = PaddingValues(8.dp)
        ) {
            items(
                items = hacks,
                key = { h -> h.hackId!! },
            ) { h -> h?.let { HackathonView(it) } }
            if (hacks.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
            item {
                AnimatedVisibility(visible = hacks.itemCount == 0) {
                    Text(
                        stringResource(id = R.string.nothing_found),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center

                    )
                }
            }
        }

    }
    LaunchedEffect(true) {
        viewModel.fetch()
    }
}



@Composable
fun HackathonTopBar(viewModel: HackathonViewModel) {
    val icon = if (viewModel.isSearchOpened) Icons.Rounded.Close else Icons.Rounded.Search
    SmallTopAppBar(title = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp, 8.dp)
        ) {
            if (viewModel.isSearchOpened)
                OutlinedTextField(
                    value = viewModel.searchText,
                    onValueChange = { viewModel.updateSearch(it) },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    textStyle = MaterialTheme.typography.bodyMedium,
                )
            else {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.nav_hackathon),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            val tag = stringResource(R.string.nav_hackathon)
            IconButton(modifier = Modifier.size(28.dp).padding(start = 8.dp),
                onClick = {
                    viewModel.isSearchOpened = !viewModel.isSearchOpened
                    if (viewModel.searchText.isNotEmpty()) {
                        viewModel.updateSearch("")
                        resetScroll(tag)
                    }
                }) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    })
}