package com.ucfjoe.teamplayers.ui.teams

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ucfjoe.teamplayers.database.entity.Team

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamItem(
    team: Team,
    onEvent: (TeamsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            onEvent(TeamsEvent.OnTeamClick(team))
        }
    ) {
        Text(
            team.name,
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}