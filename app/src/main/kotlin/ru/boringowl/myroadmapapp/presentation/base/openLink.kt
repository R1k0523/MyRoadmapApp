package ru.boringowl.myroadmapapp.presentation.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

fun openLink(url: String, context: Context) {
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    ContextCompat.startActivity(context, intent, null)
}
