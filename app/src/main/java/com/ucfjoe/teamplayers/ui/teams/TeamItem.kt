package com.ucfjoe.teamplayers.ui.teams

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        },
        colors = CardDefaults.cardColors(
            containerColor = Color(0x66FFFFFF)//.compositeOver(Color.White)
        //MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            team.name,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
        )
    }
}