package com.ucfjoe.teamplayers.ui.game_details

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.ucfjoe.teamplayers.R

@Composable
fun GameDetailsDropDownMenu(
    isGameCompleted: Boolean,
    showDropDownMenu: Boolean,
    onEvent: (GameDetailsEvent) -> Unit
) {
    DropdownMenu(
        expanded = showDropDownMenu,
        onDismissRequest = { onEvent(GameDetailsEvent.OnDismissPopupMenu) },
        modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "Reset Plays to Zero",
                    color = if (!isGameCompleted) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.outline
                )
            },
            onClick = {
                onEvent(GameDetailsEvent.OnDismissPopupMenu)
                onEvent(GameDetailsEvent.OnShowResetCountDialog)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.replay_fill_24),
                    contentDescription = "Reset all player's number of plays to zero"
                )
            },
            enabled = !isGameCompleted
        )
        Divider()
        DropdownMenuItem(
            text = {
                Text(
                    text = "Completed Game",
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            onClick = {
                onEvent(GameDetailsEvent.OnDismissPopupMenu)
                onEvent(GameDetailsEvent.OnShowCompleteGameDialog)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Complete game")
            }
        )
        Divider()
        DropdownMenuItem(
            text = {
                Text(
                    text = "Share Game Results",
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            onClick = {
                onEvent(GameDetailsEvent.OnDismissPopupMenu)
                onEvent(GameDetailsEvent.OnShareGameData)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share game results")
            }
        )
        Divider()
        DropdownMenuItem(
            text = {
                Text(
                    text = "Help",
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            onClick = {
                onEvent(GameDetailsEvent.OnDismissPopupMenu)
                onEvent(GameDetailsEvent.OnShowHelpDialog)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.help_fill_24),
                    contentDescription = "Help"
                )
            }
        )
    }
}