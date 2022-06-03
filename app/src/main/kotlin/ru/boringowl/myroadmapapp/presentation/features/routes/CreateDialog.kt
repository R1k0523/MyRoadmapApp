package ru.boringowl.myroadmapapp.presentation.features.routes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.boringowl.myroadmapapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDialog(
    viewModel: RoutesViewModel,
    snackbarHostState: SnackbarHostState
) {
    Dialog(
        onDismissRequest = { viewModel.isDialogOpened = false },
        content = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                ElevatedCard {
                    Column(Modifier.padding(24.dp, 16.dp)) {
                        Text(
                            text = "Создание плана",
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Normal,
                        )
                        OutlinedTextField(
                            value = viewModel.todoName,
                            onValueChange = { viewModel.todoName = it },
                            singleLine = true,
                            label = { Text("Название плана") },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyMedium,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Будет создан ваш личный список с навыками, " +
                                "указанными в направлении, где Вы сможете " +
                                "отмечать свой уровень знаний.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.add(
                                    onError = {
                                        viewModel.isDialogOpened = false
                                        snackbarHostState.showSnackbar(it)
                                    },
                                    onSuccess = {
                                        viewModel.isDialogOpened = false
                                        snackbarHostState.showSnackbar("Список создан")
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text(stringResource(R.string.save)) }
                        OutlinedButton(
                            onClick = {
                                viewModel.isDialogOpened = false
                                viewModel.todoName = ""
                                viewModel.routeName = ""
                                viewModel.pickedRoute = -1
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text(stringResource(R.string.cancel)) }

                    }
                }
            }
        }
    )
}