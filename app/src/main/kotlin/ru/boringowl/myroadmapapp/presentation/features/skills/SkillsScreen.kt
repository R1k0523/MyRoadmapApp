package ru.boringowl.myroadmapapp.presentation.features.skills

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
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
import ru.boringowl.myroadmapapp.model.Skill
import ru.boringowl.myroadmapapp.presentation.base.rememberForeverLazyListState
import ru.boringowl.myroadmapapp.presentation.base.resetScroll


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun SkillsScreen(
    navController: NavController,
    routeId: Int,
    viewModel: SkillsViewModel = hiltViewModel(),
) {
    Scaffold(
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
                            text = stringResource(R.string.nav_skills),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    val tag = stringResource(R.string.nav_skills)
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
        floatingActionButton = { FloatingActionButton(onClick = { viewModel.delete() }) {
        }}
    ) { p ->
        val skills = viewModel.modelList.collectAsState(listOf()).value
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
                state = rememberForeverLazyListState(stringResource(R.string.nav_routes))
            ) {
                items(skills.sortedBy { it.skillName }) { s ->
                    AnimatedVisibility(viewModel.isFiltered(s)) {
                        SkillView(s)
                    }
                }
            }
        }
    }
    LaunchedEffect(true) {
        viewModel.fetchSkills(routeId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillView(s: Skill) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                s.skillName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
            )
            Text(
                "Популярность: ${s.necessity}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light
            )
        }
    }
}


