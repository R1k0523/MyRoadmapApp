package ru.boringowl.myroadmapapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.boringowl.myroadmapapp.presentation.base.Themes
import ru.boringowl.myroadmapapp.presentation.features._root.RootScreen
import ru.boringowl.myroadmapapp.presentation.features.auth.AccountViewModel
import ru.boringowl.myroadmapapp.presentation.theme.AppTheme
import ru.boringowl.myroadmapapp.presentation.theme.HarmonizedTheme

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3Api::class)
class EntryPointActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: AccountViewModel = hiltViewModel()
            val theme = viewModel.theme.collectAsState(initial = Themes.SYSTEM.name).value
            val useDarkTheme = theme.let {
                when (it) {
                    Themes.LIGHT.name -> false
                    Themes.DARK.name -> true
                    else -> isSystemInDarkTheme()
                }
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                HarmonizedTheme(useDarkTheme) {
                    RootScreen(viewModel)
                }
            } else {
                AppTheme(useDarkTheme) {
                    RootScreen(viewModel)
                }
            }
        }
    }
}
