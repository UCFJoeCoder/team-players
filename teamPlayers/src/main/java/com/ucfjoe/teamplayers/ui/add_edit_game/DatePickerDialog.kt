package com.ucfjoe.teamplayers.ui.add_edit_game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (date: LocalDate) -> Unit,
    date: LocalDate? = null
) {
    val initialDate = (date ?: LocalDate.now())
    val initialDateMillis = TimeUnit.DAYS.toMillis(initialDate.toEpochDay()) //* 24 * 60 * 60 * 1000

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis,
        yearRange = (initialDate.year..(initialDate.year + 2))
    )

    val selectedDate = datePickerState.selectedDateMillis!!.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                //modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DatePicker(state = datePickerState, title = {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Select a Game Date",
                        style = MaterialTheme.typography.titleMedium
                    )
                })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(8.dp)
                    ) { Text("Dismiss") }
                    TextButton(
                        onClick = { onConfirmRequest(selectedDate) },
                        modifier = Modifier.padding(8.dp)
                    ) { Text("Confirm") }
                }
            }
        }
    }
}
