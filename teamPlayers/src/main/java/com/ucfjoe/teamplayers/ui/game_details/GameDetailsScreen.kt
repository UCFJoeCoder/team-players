package com.ucfjoe.teamplayers.ui.game_details

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.ui.UiEvent
import com.ucfjoe.teamplayers.ui.core.ConfirmDialog
import com.ucfjoe.teamplayers.ui.core.ObserveAsEvents
import com.ucfjoe.teamplayers.ui.formatLocalizedDateTime
import com.ucfjoe.teamplayers.ui.game_details.dialogs.EditPlayerDialog
import com.ucfjoe.teamplayers.ui.game_details.dialogs.GameDetailsHelpDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

@Composable
fun GameDetailsScreen(
    onPopBackStack: () -> Unit,
    viewModel: GameDetailsViewModel = hiltViewModel()
) {
    GameDetailsScreen(
        onPopBackStack = onPopBackStack,
        gameDetailsState = viewModel.state.value,
        uiEvent = viewModel.uiEvent,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsScreen(
    onPopBackStack: () -> Unit,
    gameDetailsState: GameDetailsState,
    uiEvent: Flow<UiEvent>,
    onEvent: (GameDetailsEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    ObserveAsEvents(flow = uiEvent, onEvent = { event ->
        when (event) {
            is UiEvent.ShowToast -> {
                Toast.makeText(context, event.message, event.duration).show()
            }

            else -> Unit
        }
    })

    GameDetailsDialogs(gameDetailsState = gameDetailsState, onEvent = onEvent)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                        gameDetailsState.team?.name ?: "",
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
                },
                actions = {
                    IconButton(onClick = { onEvent(GameDetailsEvent.OnShowPopupMenu) }) {
                        Icon(
                            Icons.Default.MoreVert, "Open additional actions menu",
                        )
                    }
                    GameDetailsDropDownMenu(
                        isGameCompleted = gameDetailsState.game?.isCompleted ?: false,
                        showDropDownMenu = gameDetailsState.showPopupMenu,
                        onEvent = onEvent
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
        {
            Column(
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 25.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = gameDetailsState.game?.gameDateTime?.formatLocalizedDateTime() ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = { onEvent(GameDetailsEvent.OnCancelSelectionClick) },
                        enabled = gameDetailsState.game?.isCompleted?.not() ?: true
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear Selection")
                        Text(text = "Clear")
                    }
                    OutlinedButton(
                        onClick = { onEvent(GameDetailsEvent.OnRepeatSelectionClick) },
                        enabled = gameDetailsState.game?.isCompleted?.not() ?: true
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.repeat_fill_24),
                            contentDescription = "Repeat Selection"
                        )
                        Text(text = "Repeat")
                    }
                    Button(
                        onClick = { onEvent(GameDetailsEvent.OnIncrementSelectionClick) },
                        enabled = gameDetailsState.game?.isCompleted?.not() ?: true
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increment Selection")
                        Text(text = "Add")
                    }
                }
                if (gameDetailsState.game?.isCompleted == true) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = "Game Completed!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 75.dp),
                    content = {
                        items(gameDetailsState.players) { player ->
                            PlayerItem(
                                player = player,
                                enabled = gameDetailsState.game?.isCompleted?.not() ?: true,
                                onSelectPlayerClick = {
                                    onEvent(GameDetailsEvent.OnSelectPlayerClick(player))
                                },
                                onEditPlayerClick = {
                                    onEvent(GameDetailsEvent.OnEditPlayerClick(player))
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun GameDetailsDialogs(
    gameDetailsState: GameDetailsState,
    onEvent: (GameDetailsEvent) -> Unit
) {
    if (gameDetailsState.showImportCurrentPlayerDialog) {
        ConfirmDialog(
            dialogTitle = "Import Players",
            dialogText = "A change in players was detected. Would you like to import the Team players to this game?\n\n" +
                    "NOTE: ALL CURRENT NUMBER OF PLAYS WILL BE LOST.\n\n" +
                    "If you proceed, the game data will be wiped and the current team players will be imported.",
            onConfirmRequest = {
                onEvent(GameDetailsEvent.OnDismissImportDialog)
                onEvent(GameDetailsEvent.OnImportPlayers)
            },
            onDismissRequest = { onEvent(GameDetailsEvent.OnDismissImportDialog) },
            icon = if (gameDetailsState.players.isNotEmpty()) Icons.Default.Warning else null
        )
    }
    if (gameDetailsState.showCompleteGameDialog) {
        ConfirmDialog(
            dialogTitle = "Mark Game as Completed",
            dialogText = getCompletedGameMessage(gameDetailsState.game?.isCompleted ?: false),
            onConfirmRequest = {
                onEvent(GameDetailsEvent.OnDismissCompleteGameDialog)
                onEvent(GameDetailsEvent.OnChangeGameCompletedState(!gameDetailsState.game!!.isCompleted))
            },
            onDismissRequest = { onEvent(GameDetailsEvent.OnDismissCompleteGameDialog) })
    }
    if (gameDetailsState.showShareGameDetailsDialog) {
        ConfirmDialog(
            dialogTitle = "Share Game Results",
            dialogText = "The current game is not marked as complete.\n\n" +
                    "By sharing the data, the game will be completed to prevent further updates.\n\n" +
                    "This can be undone from the menu.",
            onConfirmRequest = { onEvent(GameDetailsEvent.OnConfirmShareGameResults) },
            onDismissRequest = { onEvent(GameDetailsEvent.OnDismissShareGameResultsDialog) })
    }
    if (gameDetailsState.showRequestClearCountDialog) {
        ConfirmDialog(
            dialogTitle = "Set Plays to Zero",
            dialogText = "Press Confirm to set all the number of plays to zero.",
            onConfirmRequest = {
                onEvent(GameDetailsEvent.OnDismissResetCountDialog)
                onEvent(GameDetailsEvent.OnResetCountsToZero)
            },
            onDismissRequest = { onEvent(GameDetailsEvent.OnDismissResetCountDialog) })
    }
    if (gameDetailsState.showHelpDialog) {
        GameDetailsHelpDialog(onDismissRequest = { onEvent(GameDetailsEvent.OnDismissHelpDialog) })
    }
    if (gameDetailsState.showEditPlayerDialog) {
        EditPlayerDialog(
            onDismissRequest = { onEvent(GameDetailsEvent.OnHideEditPlayerDialog) },
            onConfirmRequest = { editPlayer ->
                onEvent(GameDetailsEvent.OnProcessEditPlayerRequest(editPlayer))
            },
            editPlayer = gameDetailsState.editPlayer!!,
            errorMessage = gameDetailsState.editErrorMessage
        )
    }
}

fun getCompletedGameMessage(completed: Boolean): String {
    return if (completed)
        "The game is currently marked as completed. Would you like to undo this to make updates?"
    else
        "Set the game as completed? No further updates will be allowed."
}

@Preview(showBackground = true)
@Composable
fun PreviewGameDetailsScreen() {
    GameDetailsScreen(
        onPopBackStack = { },
        gameDetailsState = GameDetailsState(
            team = Team(id = 1, name = "Knights"),
            game = Game(id = 1, teamId = 1, gameDateTime = LocalDateTime.now()),
            players = listOf(
                GamePlayer(id = 1, gameId = 1, jerseyNumber = "10"),
                GamePlayer(id = 2, gameId = 1, jerseyNumber = "23", count = 3),
                GamePlayer(id = 3, gameId = 1, jerseyNumber = "33", count = 9),
                GamePlayer(
                    id = 4,
                    gameId = 1,
                    jerseyNumber = "34",
                    count = 1,
                    isAbsent = false,
                    isSelected = true
                )
            )
        ),
        uiEvent = flow {},
        onEvent = {}
    )
}