package com.ucfjoe.teamplayers.ui.game_details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ucfjoe.teamplayers.domain.model.GamePlayer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerItem(
    player: GamePlayer,
    onSelectPlayerClick: () -> Unit,
    onEditPlayerClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .combinedClickable(
                onClick = { onSelectPlayerClick() },
                onLongClick = { onEditPlayerClick() }
            ),
        colors = CardDefaults.cardColors(
            containerColor = player.getStatusColor()
        )
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Player")
            Text(text = player.jerseyNumber, style = MaterialTheme.typography.titleLarge)
            Text(text = "Count")
            Text(text = "${player.count}", style = MaterialTheme.typography.titleMedium)
        }
    }
}