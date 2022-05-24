package ru.boringowl.myroadmapapp.presentation.features.hackathons

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.presentation.base.TextWithHeader
import ru.boringowl.myroadmapapp.presentation.base.rememberForeverLazyListState
import ru.boringowl.myroadmapapp.presentation.base.resetScroll


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun HackathonsScreen(
    navController: NavController,
    viewModel: HackathonViewModel = hiltViewModel(),
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
                            text = stringResource(R.string.nav_hackathon),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    val tag = stringResource(R.string.nav_hackathon)
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
    ) { p ->
        val hacks = viewModel.modelList.collectAsState().value.filter { viewModel.isFiltered(it) }
        Column(
            modifier = Modifier.padding(p).fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            if (hacks.isEmpty()) {
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
                state = rememberForeverLazyListState(stringResource(R.string.nav_hackathon))
            ) {
                items(hacks) { h ->
                    HackathonView(h)
                }
            }
        }
    }
    LaunchedEffect(true) {
        viewModel.fetch()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HackathonView(h: Hackathon) {
    val opened = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            h.imageUrl?.let {
                Box(
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .fillMaxSize()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it)
                            .crossfade(true)
                            .build(),
                        contentDescription = "hack_photo",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ) {
                Text(
                    h.hackTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    h.hackDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light
                )
                AnimatedVisibility(visible = opened.value) {
                    Column(Modifier.fillMaxWidth()) {
                        TextWithHeader(stringResource(R.string.hack_date), h.date)
                        TextWithHeader(stringResource(R.string.hack_focus), h.focus)
                        TextWithHeader(stringResource(R.string.hack_money), h.prize)
                        TextWithHeader(stringResource(R.string.hack_org), h.organization)
                        TextWithHeader(stringResource(R.string.hack_aud), h.routes)
                    }
                }
                Row {
                    Button(
                        onClick = { opened.value = !opened.value }, modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, end = 4.dp)
                    ) { Text(stringResource(if (opened.value) R.string.collapse else R.string.more)) }
                    Button(
                        onClick = { openLink(h.source, context) }, modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, start = 4.dp)
                    ) { Text(stringResource(R.string.go_to)) }
                }
            }
        }
    }
}


fun openLink(url: String, context: Context) {
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(context, intent, null)
}
