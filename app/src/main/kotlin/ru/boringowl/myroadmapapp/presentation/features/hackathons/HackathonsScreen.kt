package ru.boringowl.myroadmapapp.presentation.features.hackathons

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.DecayAnimation
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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
    val state = rememberForeverLazyListState(stringResource(R.string.nav_hackathon))
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = { SmallTopAppBar(title = {
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
                    Spacer(modifier = Modifier.width(8.dp))
                    val tag = stringResource(R.string.nav_hackathon)
                    IconButton(modifier = Modifier.size(28.dp),
                        onClick = {
                            viewModel.isSearchOpened = !viewModel.isSearchOpened

                            if (viewModel.searchText.isNotEmpty()) {
                                viewModel.updateSearch("")
                                resetScroll(tag)
                            }
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
            }) },
        floatingActionButtonPosition = FabPosition.End,
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
                        "Ничего не найдено",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HackathonView(h: Hackathon) {
    val opened = remember { mutableStateOf(false) }
    val context = LocalContext.current
    ElevatedCard(
        Modifier.fillMaxWidth().padding(bottom = 8.dp)
    ) {
        Column {
            h.imageUrl?.let {
                Box(
                    modifier = Modifier.background(Color.DarkGray).fillMaxSize()
                ) {
                    var placeholder by remember { mutableStateOf(true)}
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it)
                            .crossfade(true)
                            .build(),
                        onSuccess = { placeholder = false},
                        contentDescription = "hack_photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(200.dp).placeholder(visible = placeholder,
                            highlight = PlaceholderHighlight.fade(), color = MaterialTheme.colorScheme.secondaryContainer)
                    )
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
                        onClick = { openLink(h.source, context) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, end = 4.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) { Text(stringResource(R.string.go_to)) }
                    OutlinedButton(
                        onClick = { opened.value = !opened.value },
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, start = 4.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) { Text(stringResource(if (opened.value) R.string.collapse else R.string.more)) }
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
