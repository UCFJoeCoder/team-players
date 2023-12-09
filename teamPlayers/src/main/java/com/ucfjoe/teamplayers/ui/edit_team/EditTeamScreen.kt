package com.ucfjoe.teamplayers.ui.edit_team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.domain.model.Player
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.ui.add_edit_team_dialog.AddEditTeamDialog

@Composable
fun EditTeamScreen(
    onPopBackStack: () -> Unit,
    viewModel: EditTeamViewModel = hiltViewModel()
) {
    EditTeamScreen(
        onPopBackStack = onPopBackStack,
        editTeamState = viewModel.state.value,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTeamScreen(
    onPopBackStack: () -> Unit,
    editTeamState: EditTeamState,
    onEvent: (EditTeamEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (editTeamState.showEditTeamNameDialog) {
        AddEditTeamDialog(
            onDismissRequest = { onEvent(EditTeamEvent.OnHideEditTeamNameDialog) },
            onConfirmRequest = { onEvent(EditTeamEvent.OnProcessSaveTeam(it)) },
            errorMessage = editTeamState.saveError,
            initialName = editTeamState.team?.name
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    Text(
                        text = "Edit Team",
                        style = MaterialTheme.typography.headlineMedium
                            .copy(
                                fontFamily = FontFamily(Font(R.font.old_sport_college))
                            )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onPopBackStack() }) {
                        Icon(
                            painterResource(id = R.drawable.arrow_back_fill_24),
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        )
        {
            Column(
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 25.dp)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(.84f)
                    ) {
                        Text(
                            text = editTeamState.team?.name ?: "",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column(
                        modifier = Modifier.weight(.16f),
                        horizontalAlignment = Alignment.End
                    ) {
                        IconButton(
                            onClick = { onEvent(EditTeamEvent.OnShowEditTeamNameDialog) }
                        ) {
                            Icon(Icons.Default.Edit, "Edit team button")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = editTeamState.playersText,
                    onValueChange = {
                        onEvent(EditTeamEvent.OnPlayersChanged(it))
                    },
                    maxLines = 1,
                    placeholder = {
                        Text(text = "Jersey Number")
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onEvent(EditTeamEvent.OnPlayersChangedDone)
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onKeyEvent { event ->
                            when (event.key) {
                                Key.Enter -> {
                                    onEvent(EditTeamEvent.OnPlayersChangedDone)
                                    true
                                }

                                else -> false
                            }
                        }
                )
                Text(
                    text = "Players",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                        .copy(
                            fontFamily = FontFamily(Font(R.font.old_sport_college))
                        ),
                    textAlign = TextAlign.Center
                )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    content = {
                        items(editTeamState.players.size) { index ->
                            PlayerItem(
                                player = editTeamState.players[index],
                                onDeleteClick = {
                                    onEvent(
                                        EditTeamEvent.OnDeletePlayerClick(
                                            editTeamState.players[index]
                                        )
                                    )
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddEditTeamScreen() {
    EditTeamScreen(
        onPopBackStack = { },
        editTeamState = EditTeamState(
            team = Team(1, "Knights"),
            players = listOf(
                Player(1, 1, "10"),
                Player(1, 1, "23")
            )
        ),
        onEvent = {}
    )
}
