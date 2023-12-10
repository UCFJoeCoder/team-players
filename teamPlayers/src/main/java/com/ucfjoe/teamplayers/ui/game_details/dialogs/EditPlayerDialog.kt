package com.ucfjoe.teamplayers.ui.game_details.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.domain.model.GamePlayer


@Preview
@Composable
fun PreviewEditPlayerDialog() {
    EditPlayerDialog(
        onDismissRequest = { },
        onConfirmRequest = {},
        editPlayer = GamePlayer(1, 2, "02", 3),
        errorMessage = null // "Duplicate Jersey Number!"
    )
}

@Composable
fun EditPlayerDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (editPlayer: GamePlayer) -> Unit,
    editPlayer: GamePlayer,
    errorMessage: String? = null
) {
    var editJerseyNumber by remember { mutableStateOf(editPlayer.jerseyNumber) }
    var editCount by remember { mutableStateOf(editPlayer.count) }
    var editIsAbsent by remember { mutableStateOf(editPlayer.isAbsent) }

    fun processAdd(){
        if (editCount + 1 <= 100) editCount++
    }

    fun processSubtract(){
        if (editCount -1 >= 0) editCount--
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Edit Player", style = MaterialTheme.typography.headlineSmall)
                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(.8f)
                    ) {
                        Text("Jersey Number")
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        TextField(
                            value = editJerseyNumber,
                            onValueChange = { if (it.length <= 2) editJerseyNumber = it },
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text("Number of Plays")
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { processSubtract() }) {
                                Icon(
                                    painter = painterResource(R.drawable.remove_fill_24),
                                    contentDescription = "Remove"
                                )
                            }
                            Text("$editCount")
                            IconButton(onClick = { processAdd() }) {
                                Icon(Icons.Default.Add, "Add")
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.fillMaxWidth(.8f)) {
                        Text("Is Absent")
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Checkbox(checked = editIsAbsent, onCheckedChange = { editIsAbsent = it })
                    }
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Dismiss")
                    }
                    TextButton(onClick = {
                        onConfirmRequest(
                            GamePlayer(
                                editPlayer.id,
                                editPlayer.gameId,
                                editJerseyNumber,
                                editCount,
                                editIsAbsent
                            )
                        )
                    }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
