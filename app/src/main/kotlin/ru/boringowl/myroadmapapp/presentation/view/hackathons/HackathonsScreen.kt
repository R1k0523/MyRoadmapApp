package ru.boringowl.myroadmapapp.presentation.view.hackathons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.boringowl.myroadmapapp.presentation.viewmodel.AuthViewModel
import ru.boringowl.myroadmapapp.presentation.viewmodel.HackathonViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.boringowl.myroadmapapp.model.Hackathon
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun HackathonsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: HackathonViewModel = hiltViewModel(),
    ) {
    Scaffold(
        topBar = {
            SmallTopAppBar(title = {
                Column {
                    Text(
                        text = "Хакатоны",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            })
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { FloatingActionButton(onClick = {
            viewModel.add(Hackathon().also {
                it.hackId = UUID.randomUUID()
                it.source = "vk.com"
                it.imageUrl = "https://i1.sndcdn.com/avatars-ZKeowRYxnn6IsEzN-f56n4w-t500x500.jpg"
                it.hackTitle = "123123123123"
                it.hackDescription = "11111 1 23 12 3 12 312 3 1234134 13 4 134 1 34 13 4 13  1331131313 413134134"
            })
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
