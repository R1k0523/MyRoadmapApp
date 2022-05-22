package ru.boringowl.myroadmapapp.presentation.features.hackathons.list

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.presentation.base.rememberForeverLazyListState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun HackathonsScreen(
    navController: NavController,
    viewModel: HackathonViewModel = hiltViewModel(),
    ) {
    Scaffold(
        topBar = {
            SmallTopAppBar(title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        text = "Хакатоны",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = {
                            viewModel.delete()
                      },
                        content = { Text("Удалить") },
                    )
                }
            })
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.fetchAndSave() }) {
                Text("+") }
        },
    ) {
        val start = it.calculateLeftPadding(LayoutDirection.Ltr)
        val top = it.calculateTopPadding()
        val right = it.calculateRightPadding(LayoutDirection.Ltr)
        val bottom = it.calculateBottomPadding()
        val hacks = viewModel.modelList.collectAsLazyPagingItems()
        Column(
            modifier = Modifier.padding(start, top, right, bottom)
        ) {
            LazyColumn(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                state = rememberForeverLazyListState("Hackathons")
            ) {
                itemsIndexed(hacks) { i, h ->
                    HackathonView(h)
                }

                if (hacks.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HackathonView(h: Hackathon?) {
    val opened = remember { mutableStateOf(false)}
    val context = LocalContext.current
    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Box(modifier = Modifier
                .background(Color.DarkGray)
                .fillMaxSize()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(h!!.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "hack_photo",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ) {
                Text(
                    h!!.hackTitle,
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
                        TextWithHeader("Дата проведения", h.date)
                        TextWithHeader("Технологический фокус", h.focus)
                        TextWithHeader("Призовой фонд", h.prize)
                        TextWithHeader("Организатор", h.organization)
                        TextWithHeader("Целевая аудитория", h.routes)
                    }
                }
                Row {
                    Button(onClick = { opened.value = !opened.value }, modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp, end = 4.dp)) { Text(if (opened.value) "Свернуть" else "Подробнее") }
                    Button(onClick = {openLink(h.source, context)}, modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp, start = 4.dp)) { Text("Перейти") }
                }
            }
        }
    }
}
@Composable
fun TextWithHeader(header: String, text: String?) {
    if(!text.isNullOrEmpty()) {
        Column(Modifier.padding(0.dp, 6.dp)) {
            Text(
                header,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light
            )
        }
    }
}


fun openLink(url: String, context: Context) {
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(context, intent, null)
}
