package com.ucfjoe.teamplayers.ui.add_edit_team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ucfjoe.teamplayers.domain.model.Player

@Composable
fun PlayerItem(
    player: Player,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = player.jerseyNumber,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Column(
                modifier = Modifier.weight(.3f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(0.dp),
                    onClick = { onDeleteClick() }
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        "Delete"
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlayerItem() {
    PlayerItem(
        player = Player(jerseyNumber = "00", teamId = 1L),
        onDeleteClick = {}
    )
}
