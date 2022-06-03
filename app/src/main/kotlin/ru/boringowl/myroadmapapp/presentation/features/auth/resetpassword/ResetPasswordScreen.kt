package ru.boringowl.myroadmapapp.presentation.features.auth.resetpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.presentation.base.LoadingButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun ResetPasswordScreen(
    navController: NavController,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(32.dp, 0.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_myroadmap),
                    contentDescription = stringResource(R.string.logo),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                if (viewModel.state.value is ResetPasswordState.Success) {
                    OnSuccess(navController)
                } else {
                    OnDefault(navController, viewModel)
                }
            }
        }
    }
}

@Composable
fun OnDefault(navController: NavController, viewModel: ResetPasswordViewModel) {
    OutlinedTextField(
        value = viewModel.username,
        onValueChange = { viewModel.setNewUsername(it) },
        label = { Text(stringResource(R.string.username)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        isError = viewModel.isError()
    )
    OutlinedTextField(
        value = viewModel.email,
        onValueChange = { viewModel.setNewEmail(it) },
        label = { Text(stringResource(R.string.email)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        isError = viewModel.isError()
    )
    when (val state = viewModel.state.value) {
        is ResetPasswordState.Error -> Text(
            text = state.error,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        else -> Text(
            stringResource(R.string.reset_password_info),
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
    LoadingButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        onClick = { viewModel.resetPassword() },
        loading = !viewModel.isUIEnabled(),
        text = stringResource(R.string.reset_password),
    )
    Divider(
        Modifier
            .fillMaxWidth(0.6f)
            .padding(0.dp, 20.dp)
    )
    Row(
        Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp)
            .clickable { navController.popBackStack() }) {
        Text(stringResource(R.string.has_account))
        Spacer(Modifier.weight(1f))
        Text(
            text = stringResource(R.string.signin),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnSuccess(navController: NavController) {
    Text(stringResource(R.string.reset_password_success))
    Icon(
        imageVector = Icons.Rounded.Done,
        contentDescription = stringResource(R.string.signup),
        tint = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .size(70.dp)
            .padding(16.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    )
    Button(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        onClick = { navController.popBackStack() },
    ) {
        Text(stringResource(R.string.back))
    }
}