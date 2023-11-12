package com.ucfjoe.teamplayers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.Screen
import com.ucfjoe.teamplayers.database.entity.Team
import com.ucfjoe.teamplayers.viewmodels.TeamViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    navController: NavController,
    viewMode: TeamViewModel = hiltViewModel(),
    modifier:Modifier = Modifier.fillMaxSize()
    //,
    //state: DatabaseState,
    //onEvent: (TeamEvent) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 25.dp),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.CreateEditTeamScreen.route)
                //onEvent(TeamEvent.showDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Team"
                )
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val teams = listOf(
                Team( "Knights", 1L),
                Team( "Wolves", 2L),
                Team( "Seminoles", 3L),
                Team( "Tigers", 4L)
            )
            item {
                Text(
                    text = "Team Players",
                    modifier = modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.headlineMedium
                        .copy(fontFamily = FontFamily(Font(R.font.old_sport_college))),
                    textAlign = TextAlign.Center
                )
            }
            item {
                Text(
                    text = "Teams",
                    modifier = modifier,
                    style = MaterialTheme.typography.titleLarge
                )
                Divider()
            }
            for (team in teams) {
                item {
                    TeamCard(team, navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamCard(
    team: Team,
    navController: NavController
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            navController.navigate(Screen.CreateEditTeamScreen.route + "?team_id=${team.id}")
        }
    ) {
        Text(
            team.name,
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TeamScreenPreview() {
    TeamsScreen(navController = rememberNavController())
//    TeamsScreen(state = DatabaseState(), onEvent = {
//
//    })
}
