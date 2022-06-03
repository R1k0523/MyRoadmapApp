package ru.boringowl.myroadmapapp.presentation.features.books

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.FilePresent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.util.LinkifyCompat
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.model.BookInfo
import ru.boringowl.myroadmapapp.model.BookPost
import ru.boringowl.myroadmapapp.presentation.base.openLink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookView(b: BookPost) {
    val context = LocalContext.current
    var isOpened by remember { mutableStateOf(b.books.isEmpty()) }
    val mCustomLinkifyText = remember { TextView(context) }
    val header = Regex("\n *?\n").split(b.description)[0]
    val desc =
        Regex("\n *?\n").split(b.description).drop(1)
            .filter { it.trim().isNotEmpty() }
            .joinToString("\n")
    val icon = if (isOpened) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore
    val color = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

    ElevatedCard(Modifier.padding(bottom = 8.dp)) {
        Column(Modifier.padding(16.dp).clickable { isOpened = !isOpened }) {
            Column(Modifier.fillMaxWidth().padding(start = 8.dp, bottom = 8.dp)) {
                Row {
                    Text(
                        text = header,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
                    )
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(R.string.close),
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(30.dp)
                    )
                }
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
            b.books.forEach { b -> BookFileView(b) }
        }
    }
}

@Composable
fun BookFileView(b: BookInfo) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.clickable { openLink(b.url, context) }.padding(0.dp, 0.dp, 8.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.FilePresent,
            contentDescription = stringResource(R.string.search),
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
}


