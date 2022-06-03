package ru.boringowl.myroadmapapp.presentation.features._root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import ru.boringowl.myroadmapapp.R

@Composable
fun Splash() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_myroadmap),
            contentDescription = stringResource(R.string.logo),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxSize(0.8f)
                .align(Alignment.Center)
        )
    }
}