package ru.boringowl.myroadmapapp.presentation.base

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean? = null,
    loading: Boolean = false,
    text: String,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled ?: !loading,
        content = {
            Row(Modifier.height(30.dp), verticalAlignment = Alignment.CenterVertically) {
                if (loading)
                    CircularProgressIndicator(Modifier.size(30.dp))
                else
                    Text(text)
            }
        },
    )
}