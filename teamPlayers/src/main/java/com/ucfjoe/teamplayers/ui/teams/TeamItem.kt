package com.ucfjoe.teamplayers.ui.teams

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.ui.core.ConfirmDialog

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeamItem(
    team: Team,
    onEvent: (TeamsEvent) -> Unit,
    isEditMode: Boolean
) {
    val openConfirmDialog = remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onEvent(TeamsEvent.OnTeamClick(team)) },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (isEditMode) openConfirmDialog.value = true
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x66FFFFFF)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.fillMaxWidth(.9f)) {
                Text(
                    team.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                )
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                when {
                    isEditMode -> {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = com.ucfjoe.teamplayers.ui.theme.joe_theme_main_primary_alt
                        )
                    }
                }
            }
        }
    }

    when {
        openConfirmDialog.value -> {
            ConfirmDialog(
                dialogTitle = "Delete ${team.name}?",
                dialogText = "Delete the ${team.name} team and all of its related data?\n\n" +
                        "This action cannot be undone!",
                onDismissRequest = { openConfirmDialog.value = false },
                onConfirmRequest = {
                    openConfirmDialog.value = false
                    onEvent(TeamsEvent.OnDeleteClick(team))
                },
                icon = Icons.Default.Warning
            )
        }
    }
}

@Preview
@Composable
fun PreviewTeamItem() {
    TeamItem(team = Team(id = 1, name = "Knights"), onEvent = {}, isEditMode = true)
}