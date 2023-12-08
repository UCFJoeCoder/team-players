package com.ucfjoe.teamplayers.ui.add_edit_game

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@Preview
@Composable
fun DatePickerDialog(){
    DatePickerDialog(
        {},
        {},
        LocalDate.now().minusDays(-14)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (date: LocalDate) -> Unit,
    initialDate: LocalDate? = null
) {
    val date = (initialDate ?: LocalDate.now())
    val dateMillis = TimeUnit.DAYS.toMillis(date.toEpochDay()) //* 24 * 60 * 60 * 1000

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateMillis,
        yearRange = (date.year..(date.year + 2))
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
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DatePicker(state = datePickerState, title = {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Game Date",
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
