package com.ucfjoe.teamplayers.ui.add_edit_game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.ui.NavEvent
import com.ucfjoe.teamplayers.ui.core.ObserveAsEvents
import com.ucfjoe.teamplayers.ui.formatLocalizedDate
import com.ucfjoe.teamplayers.ui.formatLocalizedTime

@Composable
fun AddEditGameScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (NavEvent.Navigate) -> Unit,
    viewModel: AddEditGameViewModel = hiltViewModel()
) {
    ObserveAsEvents(flow = viewModel.navEvent, onEvent = { event ->
        when (event) {
            is NavEvent.Navigate -> onNavigate(event)
            is NavEvent.PopBackStack -> onPopBackStack()
        }
    })

    AddEditGameScreen(
        onPopBackStack = onPopBackStack,
        addEditGameState = viewModel.state.value,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditGameScreen(
    onPopBackStack: () -> Unit,
    addEditGameState: AddEditGameState,
    onEvent: (AddEditGameEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (addEditGameState.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { onEvent(AddEditGameEvent.OnHideDatePicker) },
            onConfirmRequest = { onEvent(AddEditGameEvent.OnDateChanged(it)) },
            initialDate = addEditGameState.date
        )
    }

    if (addEditGameState.showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { onEvent(AddEditGameEvent.OnHideTimePicker) },
            onConfirmRequest = {
                onEvent(AddEditGameEvent.OnTimeChanged(it))
            },
            initialTime = addEditGameState.time
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxWidth(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(AddEditGameEvent.OnSaveGameClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Game"
                )
            }
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    Text(
                        text = getScreenTitle(addEditGameState.isEditMode),
                        style = MaterialTheme.typography.headlineMedium
                            .copy(
                                fontFamily = FontFamily(Font(R.font.old_sport_college))
                            )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onPopBackStack() }) {
                        Icon(
                            painterResource(id = R.drawable.arrow_back_fill_24),
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 25.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = addEditGameState.team?.name ?: "",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleLarge
                )
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.fillMaxWidth(.5f)) {
                        Text(
                            text = addEditGameState.date.formatLocalizedDate()
                        )
                    }
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Button(onClick = { onEvent(AddEditGameEvent.OnShowDatePicker) }) {
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
                            text = addEditGameState.time.formatLocalizedTime()
                        )
                    }
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Button(onClick = { onEvent(AddEditGameEvent.OnShowTimePicker) }) {
                            Text(
                                "Select Time"
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getScreenTitle(isEditMode: Boolean): String {
    return if (isEditMode) "Edit Game" else "Add Game"
}

@Preview(showBackground = true)
@Composable
fun PreviewAddEditGameScreen() {
    AddEditGameScreen(
        onPopBackStack = { },
        addEditGameState = AddEditGameState(
            teamId = 0,
            team = Team(1, "Knights")
        ),
        onEvent = {}
    )
}