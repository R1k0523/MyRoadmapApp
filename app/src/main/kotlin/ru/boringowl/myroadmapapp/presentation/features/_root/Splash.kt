package ru.boringowl.myroadmapapp.presentation.features._root

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.boringowl.myroadmapapp.R

@Composable
fun Splash() {
    var enabled by remember { mutableStateOf(false) }
    val alpha = animateDpAsState(if (enabled) 200.dp else 0.dp, tween(1000))
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .fillMaxHeight(0.3f),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_icon_with_back),
                contentDescription = stringResource(R.string.logo),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(48.dp)
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_myroadmap),
                contentDescription = stringResource(R.string.logo),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .width(alpha.value)
                    .fillMaxHeight()
                    .padding(start = 16.dp)
            )
        }
    }
    LaunchedEffect(true) {
        delay(300)
        enabled = true
    }
}