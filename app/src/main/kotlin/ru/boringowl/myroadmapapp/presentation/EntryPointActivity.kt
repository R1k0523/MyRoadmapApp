package ru.boringowl.myroadmapapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import dagger.hilt.android.AndroidEntryPoint
import ru.boringowl.myroadmapapp.presentation.features._root.RootScreen
import ru.boringowl.myroadmapapp.presentation.theme.MyroadmapappTheme

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3Api::class)
class EntryPointActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyroadmapappTheme {
                RootScreen()
            }
        }
    }
}

