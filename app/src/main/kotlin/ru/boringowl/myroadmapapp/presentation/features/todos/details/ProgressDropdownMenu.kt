package ru.boringowl.myroadmapapp.presentation.features.todos.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import ru.boringowl.myroadmapapp.R

@Composable
fun ProgressDropdownMenu(initial: Int, onChoose: (value: Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val items = stringArrayResource(R.array.progress)
    var selectedIndex by remember { mutableStateOf(initial) }
    val focuser = LocalFocusManager.current
    val icon = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
    ) {
        OutlinedTextField(
            value = items[selectedIndex],
            onValueChange = {},
            label = { Text(stringResource(R.string.level)) },
            singleLine = false,
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    expanded = it.hasFocus
                },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        )
        DropdownMenu(
            modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.80f),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                focuser.clearFocus()
            },
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expanded = false
                        focuser.clearFocus()
                        onChoose(selectedIndex)
                    },
                    text = { Text(s) }
                )
            }
        }
    }
}
