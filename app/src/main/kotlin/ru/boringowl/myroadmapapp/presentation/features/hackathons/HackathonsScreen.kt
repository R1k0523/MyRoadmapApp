package ru.boringowl.myroadmapapp.presentation.features.hackathons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.boringowl.myroadmapapp.presentation.features.auth.login.LoginViewModel

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
                        onClick = {viewModel.delete()},
                        content = { Text("Удалить") },
                    )
                }
            })
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { FloatingActionButton(onClick = {
            viewModel.fetchAndSave()
        }){
            Text("+")
        } },
        bottomBar = { BottomAppBar() { Text("BottomAppBar") } }

    ) {
        val start = it.calculateLeftPadding(LayoutDirection.Ltr)
        val top = it.calculateTopPadding()
        val right = it.calculateRightPadding(LayoutDirection.Ltr)
        val bottom = it.calculateBottomPadding()
        val hacks = viewModel.modelList.collectAsState().value

        Column(
            modifier = Modifier.padding(start, top, right, bottom)
        ) {
            LazyColumn(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                println(viewModel.modelList.value.size)
                items(hacks) { h ->
                    Card(
                        Modifier
                            .fillMaxWidth(0.8f)
                            .padding(16.dp)) {
                        Column {
                            Text(h.hackTitle)
                            Text(h.hackDescription)
                            Text(h.source)
                            Box(modifier = Modifier.background(Color.DarkGray).fillMaxSize()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(h.imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "hack_photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
