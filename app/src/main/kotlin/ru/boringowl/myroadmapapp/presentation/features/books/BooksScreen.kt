package ru.boringowl.myroadmapapp.presentation.features.books

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.text.util.LinkifyCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.data.room.model.BookPostEntity
import ru.boringowl.myroadmapapp.model.BookPost
import ru.boringowl.myroadmapapp.model.Skill
import ru.boringowl.myroadmapapp.model.utils.format
import ru.boringowl.myroadmapapp.presentation.base.rememberForeverLazyListState
import ru.boringowl.myroadmapapp.presentation.base.resetScroll
import ru.boringowl.myroadmapapp.presentation.features.hackathons.HackathonView


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun BooksScreen(
    navController: NavController,
    routeId: Int,
    viewModel: BooksViewModel = hiltViewModel(),
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
                            onValueChange = { viewModel.updateSearch(it, routeId) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            textStyle = MaterialTheme.typography.bodyMedium,
                        )
                    else {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.nav_books),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    val tag = stringResource(R.string.nav_books)
                    IconButton(modifier = Modifier.size(28.dp),
                        onClick = {
                            viewModel.isSearchOpened = !viewModel.isSearchOpened
                            if (viewModel.searchText.isNotEmpty()) {
                                viewModel.updateSearch("", routeId)
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
            })
        }
    ) { p ->
        val books = viewModel.modelList.collectAsLazyPagingItems()
        Column(
            modifier = Modifier
                .padding(p)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = books.itemCount == 0) {
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
                contentPadding = PaddingValues(8.dp)
            ) {
                items(
                    items = books,
                    key = { b -> b.bookPostId!! },
                ) { b -> b?.let { BookView(it) } }
                if (books.loadState.append == LoadState.Loading) {
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
    LaunchedEffect(true) {
        viewModel.fetch(routeId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookView(b: BookPost) {
    val context = LocalContext.current
    var isOpened by remember { mutableStateOf(b.books.isEmpty()) }
    val mCustomLinkifyText = remember { TextView(context) }
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clickable { isOpened = !isOpened }
                .padding(16.dp)) {
            val header = Regex("\n *?\n").split(b.description)[0]
            val desc =
                Regex("\n *?\n").split(b.description).drop(1)
                    .filter { it.trim().isNotEmpty() }
                    .joinToString("\n")
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)) {
                Row {
                    Text(
                        header,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
                    )
                    Icon(
                        if (isOpened) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                        "Закрыть",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
                val color = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
                AnimatedVisibility(isOpened) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        AndroidView(factory = { mCustomLinkifyText }) { textView ->
                            textView.text = desc
                            textView.textSize = 14f
                            textView.setTextColor(color)
                            LinkifyCompat.addLinks(textView, Linkify.ALL)
                            textView.movementMethod = LinkMovementMethod.getInstance()
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            b.books.forEach { b ->
                Row(
                    Modifier
                        .clickable { openLink(b.url, context) }
                        .padding(0.dp, 0.dp, 8.dp, 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Rounded.FilePresent,
                        "Найти",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                    Column {
                        Text(
                            b.filename,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            b.sizeString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Light,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}


fun openLink(url: String, context: Context) {
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    ContextCompat.startActivity(context, intent, null)

}

