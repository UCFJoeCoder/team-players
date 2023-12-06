package com.ucfjoe.teamplayers.ui.team_details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.ui.core.ConfirmDialog
import com.ucfjoe.teamplayers.ui.formatLocalizedDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameItem(
    game: Game,
    onEvent: (TeamDetailsEvent) -> Unit,
    isEditMode: Boolean
) {
    var openConfirmDialog by remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current

    if (openConfirmDialog) {
        ConfirmDialog(
            dialogTitle = "Delete Game?",
            dialogText = "Delete the Game and all its related data?",
            onDismissRequest = { openConfirmDialog = false },
            onConfirmRequest = {
                openConfirmDialog = false
                onEvent(TeamDetailsEvent.OnDeleteClick(game))
            },
            icon = Icons.Default.Warning
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onEvent(TeamDetailsEvent.OnGameClick(game)) },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (isEditMode) openConfirmDialog = true
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(.90f)) {
                Text(
                    text = game.gameDateTime.formatLocalizedDateTime(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }
            Column(Modifier.weight(.10f), horizontalAlignment = Alignment.End) {
                when {
                    isEditMode -> {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }
        }
    }
}