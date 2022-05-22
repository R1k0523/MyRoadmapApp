package ru.boringowl.myroadmapapp.presentation.base

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import ru.boringowl.myroadmapapp.R

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = { Text(stringResource(R.string.password)) },
    placeholder: @Composable (() -> Unit)? = null,
    trailingIconImageVector: ImageVector = when {
        passwordVisible -> Icons.Filled.Visibility
        else -> Icons.Filled.VisibilityOff
    },
    trailingIconContentDescription: String = when {
        passwordVisible -> stringResource(R.string.hide_password)
        else -> stringResource(R.string.show_password)
    },
    isInvalid: Boolean = false,
) = OutlinedTextField(
    value = password,
    onValueChange = onPasswordChange,
    label = label,
    placeholder = placeholder,
    singleLine = true,
    modifier = modifier,
    visualTransformation = when {
        passwordVisible -> VisualTransformation.None
        else -> PasswordVisualTransformation()
    },
    colors = TextFieldDefaults.outlinedTextFieldColors(
        unfocusedBorderColor = if (isInvalid)
            MaterialTheme.colorScheme.error else
            MaterialTheme.colorScheme.onBackground
    ),
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    trailingIcon = {
        IconButton(onClick = { onPasswordVisibleChange(!passwordVisible) }) {
            Icon(trailingIconImageVector, trailingIconContentDescription)
        }
    },
)