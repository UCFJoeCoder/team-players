package com.ucfjoe.teamplayers.ui.add_edit_game.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalTime

@Preview
@Composable
fun TimePickerDialog(){
    TimePickerDialog(
        {},
        {},
        LocalTime.now()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (date: LocalTime) -> Unit,
    initialTime: LocalTime? = null
) {
    val localTime = initialTime ?: LocalTime.now()

    val timePickerState = rememberTimePickerState(
        initialHour = localTime.hour,
        initialMinute = localTime.minute,
        is24Hour = false
    )

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Game Time",
                    style = MaterialTheme.typography.titleMedium
                )
                TimePicker(state = timePickerState)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(8.dp)
                    ) { Text("Dismiss") }
                    TextButton(
                        onClick = {
                            onConfirmRequest(
                                LocalTime.of(
                                    timePickerState.hour,
                                    timePickerState.minute
                                )
                            )
                        },
                        modifier = Modifier.padding(8.dp)
                    ) { Text("Confirm") }
                }
            }
        }
    }
}