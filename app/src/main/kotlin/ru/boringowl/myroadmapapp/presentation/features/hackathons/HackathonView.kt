package ru.boringowl.myroadmapapp.presentation.features.hackathons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.presentation.base.TextWithHeader
import ru.boringowl.myroadmapapp.presentation.base.openLink

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