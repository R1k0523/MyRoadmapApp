package ru.boringowl.myroadmapapp.presentation.features.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.model.Route
import ru.boringowl.myroadmapapp.presentation.base.*
import ru.boringowl.myroadmapapp.presentation.features.auth.AccountViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState, snackbar = { Snackbar(snackbarData = it) }) },
        topBar = {
            SmallTopAppBar(title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp, 8.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.nav_profile),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(modifier = Modifier.size(28.dp),
                        onClick = {
                            accountViewModel.logOut()
                        }) {
                        Icon(
                            Icons.Rounded.Logout,
                            stringResource(R.string.logout),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            })
        },
    ) { p ->

        Column(
            modifier = Modifier
                .padding(paddingValues = p)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(Modifier.fillMaxWidth(0.8f)) {
                OutlinedTextField(
                    value = viewModel.usernameText,
                    onValueChange = {},
                    enabled = false,
                    label = { Text(stringResource(R.string.username)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                EditableTextField(
                    value = viewModel.emailText,
                    onValueChange = {
                        viewModel.emailText = it
                    },
                    enabled = viewModel.isEmailChanging,
                    label = { Text(stringResource(R.string.email)) },
                    onTextEditableChange = {
                        viewModel.isEmailChanging = it
                        if (!it) viewModel.resetEmail()
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                AnimatedVisibility(visible = viewModel.isEmailChanging) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        LoadingButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.update {
                                    focusManager.clearFocus()
                                    snackbarHostState.showSnackbar(viewModel.snackBarText())
                                }
                            },
                            loading = !viewModel.isUIEnabled(),
                            text = stringResource(R.string.save)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedVisibility(visible = viewModel.isPasswordChanging) {
                    Column(Modifier.fillMaxWidth()) {
                        PasswordTextField(
                            password = viewModel.passwordText,
                            onPasswordChange = { viewModel.setPassword(it) },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.old_password)) },
                            passwordVisible = viewModel.passwordVisible,
                            onPasswordVisibleChange = { viewModel.passwordVisible = it },
                            isInvalid = viewModel.isError()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PasswordTextField(
                            password = viewModel.newPasswordText,
                            onPasswordChange = { viewModel.setNewPassword(it) },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.new_password)) },
                            passwordVisible = viewModel.newPasswordVisible,
                            onPasswordVisibleChange = { viewModel.newPasswordVisible = it },
                            isInvalid = viewModel.isError()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LoadingButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { viewModel.updatePassword {
                                focusManager.clearFocus()
                                snackbarHostState.showSnackbar(viewModel.snackBarText())
                            }  },
                            loading = !viewModel.isUIEnabled(),
                            text = stringResource(R.string.save),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                LoadingButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.isPasswordChanging = !viewModel.isPasswordChanging },
                    loading = !viewModel.isUIEnabled(),
                    text = stringResource(
                        if (viewModel.isPasswordChanging)
                            R.string.cancel
                        else
                            R.string.update_password)
                )
            }

        }

    }
    LaunchedEffect(true) {
        viewModel.fetch()
    }
}
