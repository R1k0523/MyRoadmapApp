package ru.boringowl.myroadmapapp.presentation.features.auth.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import ru.boringowl.myroadmapapp.presentation.base.PasswordTextField

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel = hiltViewModel()) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(32.dp, 0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(Modifier.fillMaxWidth().fillMaxHeight(0.3f)) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_myroadmap),
                    contentDescription = "Logo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Column(
                Modifier.fillMaxWidth().fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.setNewUsername(it) },
                    label = { Text(stringResource(R.string.username)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = if (viewModel.isError())
                            MaterialTheme.colorScheme.error else
                            MaterialTheme.colorScheme.onBackground
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.setNewEmail(it) },
                    label = { Text(stringResource(R.string.email)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = if (viewModel.isError())
                            MaterialTheme.colorScheme.error else
                            MaterialTheme.colorScheme.onBackground
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))

                PasswordTextField(
                    password = viewModel.password,
                    onPasswordChange = { viewModel.setNewPassword(it) },
                    modifier = Modifier.fillMaxWidth(),
                    passwordVisible = viewModel.passwordVisible,
                    onPasswordVisibleChange = { viewModel.passwordVisible = it },
                    isInvalid = viewModel.isError()
                )
                Spacer(modifier = Modifier.height(8.dp))

                PasswordTextField(
                    password = viewModel.passwordAgain,
                    onPasswordChange = { viewModel.setAgainPassword(it) },
                    label = { Text(stringResource(R.string.passwordAgain)) },
                    modifier = Modifier.fillMaxWidth(),
                    passwordVisible = viewModel.passwordAgainVisible,
                    onPasswordVisibleChange = { viewModel.passwordAgainVisible = it },
                    isInvalid = viewModel.isError() || !viewModel.passwordsAreSimilar()
                )
                Spacer(modifier = Modifier.height(16.dp))
                when (val state = viewModel.state.value) {
                    is SignUpState.Error -> Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    else -> {}
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.signUp() },
                    enabled = viewModel.isUIEnabled() && viewModel.passwordsAreSimilar(),
                    content = {
                        if (viewModel.isUIEnabled())
                            Text(stringResource(R.string.signup))
                        else
                            CircularProgressIndicator()
                    },
                )
                Divider(Modifier.fillMaxWidth(0.6f).padding(0.dp, 20.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { navController.popBackStack() }
                        .padding(0.dp, 8.dp)) {
                    Text(
                        text = stringResource(R.string.has_account)
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.signin),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
