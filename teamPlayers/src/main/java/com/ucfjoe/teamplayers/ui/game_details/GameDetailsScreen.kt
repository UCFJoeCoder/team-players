package com.ucfjoe.teamplayers.ui.game_details

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.ui.UiEvent
import com.ucfjoe.teamplayers.ui.core.ConfirmDialog
import com.ucfjoe.teamplayers.ui.formatLocalizedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    gameDetailsState: GameDetailsState,
    onEvent: (GameDetailsEvent) -> Unit
) {
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
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
