package ru.boringowl.myroadmapapp.presentation.base

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import ru.boringowl.myroadmapapp.R

@Composable
fun EditableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    onTextEditableChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = { Text(stringResource(R.string.password)) },
    placeholder: @Composable (() -> Unit)? = null,
    trailingIconImageVector: ImageVector = when {
        enabled -> Icons.Rounded.Cancel
        else -> Icons.Rounded.Edit
    },
    trailingIconContentDescription: String = when {
        enabled -> stringResource(R.string.change_field)
        else -> stringResource(R.string.cancel)
    },
    isInvalid: Boolean = false,
) = OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = label,
    placeholder = placeholder,
    singleLine = true,
    modifier = modifier,
    isError = isInvalid,
    enabled = enabled,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    trailingIcon = {
        IconButton(onClick = { onTextEditableChange(!enabled) }) {
            Icon(trailingIconImageVector, trailingIconContentDescription)
        }
    },
)