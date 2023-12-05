package com.ucfjoe.teamplayers.ui.add_edit_game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.ui.UiEvent
import com.ucfjoe.teamplayers.ui.formatLocalizedDate
import com.ucfjoe.teamplayers.ui.formatLocalizedTime

@Composable
fun AddEditGameScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: AddEditGameViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (viewModel.state.value.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(AddEditGameEvent.OnHideDatePicker) },
            onConfirmRequest = {
                viewModel.onEvent(AddEditGameEvent.OnDateChanged(it))
            },
            date = viewModel.state.value.date
        )
    }

    if (viewModel.state.value.showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { viewModel.onEvent(AddEditGameEvent.OnHideTimePicker) },
            onConfirmRequest = {
                viewModel.onEvent(AddEditGameEvent.OnTimeChanged(it))
            },
            localTime = viewModel.state.value.time
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()

                is UiEvent.Navigate -> onNavigate(event)

                else -> {
                    println("Unhandled event ${event}")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 25.dp),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditGameEvent.OnSaveGameClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Game"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            Text(
                text = getScreenTitle(viewModel.state.value.isEditMode),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.headlineMedium
                    .copy(
                        fontFamily = FontFamily(Font(R.font.old_sport_college))
                    ),
                textAlign = TextAlign.Center
            )
            Text(
                text = viewModel.state.value.team?.name ?: "",
                modifier = Modifier,
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.fillMaxWidth(.5f)) {
                    Text(
                        text = viewModel.state.value.date.formatLocalizedDate()
                    )
                }
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Button(onClick = { viewModel.onEvent(AddEditGameEvent.OnShowDatePicker) }) {
                        Text(
                            "Select Date"
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.fillMaxWidth(.5f)) {
                    Text(
                        text = viewModel.state.value.time.formatLocalizedTime()
                    )
                }
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Button(onClick = { viewModel.onEvent(AddEditGameEvent.OnShowTimePicker) }) {
                        Text(
                            "Select Time"
                        )
                    }
                }
            }
        }
    }
}

fun getScreenTitle(isEditMode: Boolean): String {
    return if (isEditMode) "Edit Game" else "Add Game"
}
