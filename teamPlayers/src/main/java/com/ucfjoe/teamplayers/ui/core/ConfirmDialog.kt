package com.ucfjoe.teamplayers.ui.core

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ConfirmDialog(
    dialogTitle: String,
    dialogText: String,
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    icon: ImageVector? = null
) {
    AlertDialog(
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        icon = {
            if (icon != null) Icon(icon, contentDescription = "Confirmation Dialog Icon")
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmRequest() }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Dismiss")
            }
        }
    )
}