package com.ucfjoe.teamplayers.ui.game_details

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.ucfjoe.teamplayers.R

@Composable
fun GameDetailsDropDownMenu(
    showDropDownMenu: Boolean,
    onEvent: (GameDetailsEvent) -> Unit
){
    DropdownMenu(
        expanded = showDropDownMenu,
        onDismissRequest = { onEvent(GameDetailsEvent.OnHidePopupMenu) },
        modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "Import Players from Team",
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            onClick = {
                onEvent(GameDetailsEvent.OnHidePopupMenu)
                onEvent(GameDetailsEvent.OnRequestImportPlayers)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.system_update_alt_fill_24),
                    contentDescription = "Import players from team"
                )
            }
        )
        HorizontalDivider()
        DropdownMenuItem(
            text = {
                Text(
                    text = "Reset Counts to Zero",
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            onClick = {
                onEvent(GameDetailsEvent.OnHidePopupMenu)
                onEvent(GameDetailsEvent.OnShowRequestClearCountDialog)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.replay_fill_24),
                    contentDescription = "Reset all player's number of plays to zero"
                )
            }
        )
        HorizontalDivider()
        DropdownMenuItem(
            text = {
                Text(
                    text = "Share Game Results",
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            onClick = {
                onEvent(GameDetailsEvent.OnHidePopupMenu)
                onEvent(GameDetailsEvent.OnShareGameResults)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share game results")
            }
        )
        HorizontalDivider()
        DropdownMenuItem(
            text = {
                Text(
                    text = "Help",
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            onClick = {
                onEvent(GameDetailsEvent.OnHidePopupMenu)
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