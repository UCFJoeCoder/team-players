package com.ucfjoe.teamplayers.ui.teams

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.database.entity.Team
import com.ucfjoe.teamplayers.ui.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TeamsViewModel = hiltViewModel()
) {
    val teams = viewModel.teams.collectAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    // TODO("remove if this is not going to be used")
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }

                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 25.dp),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TeamsEvent.OnAddTeamClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Team"
                )
            }
        }
    ) { padding ->

//        val teams = listOf(
//            Team( "Knights", 1L),
//            Team( "Wolves", 2L),
//            Team( "Seminoles", 3L),
//            Team( "Tigers", 4L)
//        )

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth())
        {
            Text(
                text = "Team Players",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium
                    .copy(fontFamily = FontFamily(Font(R.font.old_sport_college))),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Teams",
                modifier = Modifier,
                style = MaterialTheme.typography.titleLarge
            )
            Divider()
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(teams.value) { team ->
                    TeamItem(
                        team,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onEvent(TeamsEvent.OnTeamClick(team))
                            }
                            .padding(16.dp, 0.dp)
                    )
                }
            }
        }
    }
}


// TODO() try to get the preview to work with dependency injection
@Preview(showBackground = true)
@Composable
fun TeamScreenPreview() {
    TeamsScreen(onNavigate = { })
}
