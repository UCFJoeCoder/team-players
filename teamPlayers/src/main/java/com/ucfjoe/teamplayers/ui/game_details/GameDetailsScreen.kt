package com.ucfjoe.teamplayers.ui.game_details

import android.util.Log
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.ui.UiEvent
import com.ucfjoe.teamplayers.ui.core.ConfirmDialog
import com.ucfjoe.teamplayers.ui.formatLocalizedDateTime
import java.time.LocalDateTime

@Composable
fun GameDetailsScreen(
    onPopBackStack: () -> Unit,
    viewModel: GameDetailsViewModel = hiltViewModel()
) {
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)

    GameDetailsScreen(
        onPopBackStack = onPopBackStack,
        gameDetailsState = viewModel.state.value,
        uiEvent = uiEvent.value,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsScreen(
    onPopBackStack: () -> Unit,
    gameDetailsState: GameDetailsState,
    uiEvent: UiEvent?,
    onEvent: (GameDetailsEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    if (gameDetailsState.askToImportCurrentPlayer) {
        ConfirmDialog(
            dialogTitle = "Import Players",
            dialogText = getRequestImportPlayersMessage(gameDetailsState.players.isEmpty()),
            onConfirmRequest = {
                onEvent(GameDetailsEvent.OnDismissImportDialog)
                onEvent(GameDetailsEvent.OnImportPlayers)
            },
            onDismissRequest = { onEvent(GameDetailsEvent.OnDismissImportDialog) },
            icon = if (gameDetailsState.players.isNotEmpty()) Icons.Default.Warning else null
        )
    }
    if (gameDetailsState.showShareGameDetailsDialog) {
        ConfirmDialog(
            dialogTitle = "Share Game Results",
            dialogText = "This feature is not implemented yet",
            onConfirmRequest = { onEvent(GameDetailsEvent.OnHideShareGameResults) },
            onDismissRequest = { onEvent(GameDetailsEvent.OnHideShareGameResults) })
    }
    if (gameDetailsState.showRequestClearCountDialog) {
        ConfirmDialog(
            dialogTitle = "Reset Counts to Zero",
            dialogText = "Press Confirm to reset all the number of plays to zero.",
            onConfirmRequest = {
                onEvent(GameDetailsEvent.OnHideRequestClearCountDialog)
                onEvent(GameDetailsEvent.OnResetCountsToZero)
            },
            onDismissRequest = { onEvent(GameDetailsEvent.OnHideRequestClearCountDialog) })
    }
    if (gameDetailsState.showHelpDialog) {
        GameDetailsHelpDialog(onDismissRequest = { onEvent(GameDetailsEvent.OnHideHelpDialog) })
    }

    LaunchedEffect(key1 = uiEvent) {
        Log.e("GameDetailsScreen", "REcieved")
        when (uiEvent) {
            is UiEvent.ShowToast -> {
                Log.e("asdf", "ShowToast")
                Toast.makeText(context, uiEvent.message, uiEvent.duration).show()
            }

            else -> Unit
        }
    }

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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = { onEvent(GameDetailsEvent.OnCancelSelectionClick) }
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear Selection")
                        Text(text = "Clear")
                    }
                    OutlinedButton(onClick = { onEvent(GameDetailsEvent.OnRepeatSelectionClick) }) {
                        Icon(
                            painter = painterResource(R.drawable.repeat_fill_24),
                            contentDescription = "Repeat Selection"
                        )
                        Text(text = "Repeat")
                    }
                    Button(onClick = { onEvent(GameDetailsEvent.OnIncrementSelectionClick) }) {
                        Icon(Icons.Default.Add, contentDescription = "Increment Selection")
                        Text(text = "Add")
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 75.dp),
                    content = {
                        items(gameDetailsState.players) { player ->
                            PlayerItem(
                                player = player,
                                onSelectPlayerClick = {
                                    onEvent(GameDetailsEvent.OnSelectPlayerClick(player))
                                },
                                onEditPlayerClick = {
                                    // TODO ("Handle edit player request")
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

fun getRequestImportPlayersMessage(isListEmpty: Boolean): String {
    return if (isListEmpty)
        "This game does not have any players.\n\nWould you like to import the current players from the team?"
    else
        "If you proceed, all current players will be removed from this game.\n\nAll the current players on the team will be imported."
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
        uiEvent = null,
        onEvent = {}
    )
}