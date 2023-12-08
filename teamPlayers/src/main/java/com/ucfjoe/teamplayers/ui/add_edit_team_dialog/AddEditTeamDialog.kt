package com.ucfjoe.teamplayers.ui.add_edit_team_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddEditTeamDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (name: String) -> Unit,
    errorMessage: String?,
    initialName: String? = null
) {
    var nameText by remember { mutableStateOf(initialName ?: "") }

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Please enter a Team Name")
                TextField(
                    //modifier = Modifier.weight(.70f),
                    value = nameText,
                    onValueChange = { if (it.length <= 15) nameText = it },
                    singleLine = true,
                    placeholder = {
                        Text(text = "Team Name")
                    },
                    isError = errorMessage.isNullOrBlank().not(),
                    supportingText = { errorMessage?.let { Text(text = it) } }
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Dismiss")
                    }
                    TextButton(onClick = { onConfirmRequest(nameText) }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}