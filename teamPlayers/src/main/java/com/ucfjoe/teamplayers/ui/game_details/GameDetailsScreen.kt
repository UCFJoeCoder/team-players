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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            dialogText = "This game does not have any players. Would you like to import the current players from the team?",
            onConfirmRequest = { onEvent(GameDetailsEvent.OnImportPlayersRequest) },
            onDismissRequest = { onEvent(GameDetailsEvent.OnDismissImportDialog) }
        )
    }
    var showShareGameResultsDialog by remember { mutableStateOf(false) }
    if (showShareGameResultsDialog) {
        ConfirmDialog(
            dialogTitle = "Share Game Results",
            dialogText = "This feature is not implemented yet",
            onConfirmRequest = { showShareGameResultsDialog = false },
            onDismissRequest = { showShareGameResultsDialog = false })
    }

    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
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
                    DropdownMenu(
                        expanded = gameDetailsState.showPopupMenu,
                        onDismissRequest = { onEvent(GameDetailsEvent.OnHidePopupMenu) }) {
                        DropdownMenuItem(
                            text = { Text("Clear Selections") },
                            onClick = {
                                onEvent(GameDetailsEvent.OnHidePopupMenu)
                                onEvent(GameDetailsEvent.OnCancelSelectionClick)
                            },
                        )
                        DropdownMenuItem(
                            text = { Text("Import Players from Team") },
                            onClick = { onEvent(GameDetailsEvent.OnHidePopupMenu) },
                        )
                        DropdownMenuItem(
                            text = { Text("Reset Counts to Zero") },
                            onClick = {
                                onEvent(GameDetailsEvent.OnHidePopupMenu)
                                onEvent(GameDetailsEvent.OnResetCountsToZeroRequest)
                            },
                        )
                        DropdownMenuItem(
                            text = { Text("Send Game Results") },
                            onClick = {
                                onEvent(GameDetailsEvent.OnHidePopupMenu)
                                onEvent(GameDetailsEvent.OnShareGameResults)
                                showShareGameResultsDialog = true
                            },
                        )
                    }
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
