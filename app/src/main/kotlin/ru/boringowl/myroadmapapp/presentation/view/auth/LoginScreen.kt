package ru.boringowl.myroadmapapp.presentation.view.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.presentation.view.components.PasswordTextField
import ru.boringowl.myroadmapapp.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Column {
                    Text(
                        text = "MyRoadmap",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            })
        }
    ) {
        val start = it.calculateLeftPadding(LayoutDirection.Ltr)
        val top = it.calculateTopPadding()
        val right = it.calculateRightPadding(LayoutDirection.Ltr)
        val bottom = it.calculateBottomPadding()
        Column(
            modifier = Modifier.padding(start, top, right, bottom)
        ) {
            Column(
                Modifier.fillMaxWidth().fillMaxHeight(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Вход",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                OutlinedTextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.username = it },
                    label = { Text(stringResource(R.string.username)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                )
                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextField(
                    password = viewModel.password,
                    onPasswordChange = { viewModel.password = it },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    passwordVisible = viewModel.passwordVisible,
                    onPasswordVisibleChange = { viewModel.passwordVisible = it },
                )
                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                        append("Забыли пароль?")
                    }
                }
                ClickableText(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(5.dp),
                    text = annotatedString,
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    onClick = {},
                    content = { Text("Войти") },
                )
            }
            Column(
                Modifier.fillMaxWidth().fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FilledIconButton(modifier = Modifier.size(48.dp),
                    onClick = { }) {
                    Icon(
                        Icons.Rounded.ArrowForward,
                        "Зарегистрироваться",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Впервые здесь?"
                )
            }
        }
    }
}
