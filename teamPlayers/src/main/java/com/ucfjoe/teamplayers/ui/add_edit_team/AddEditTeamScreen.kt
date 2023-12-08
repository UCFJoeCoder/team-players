package com.ucfjoe.teamplayers.ui.add_edit_team

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun AddEditTeamScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTeamViewModel = hiltViewModel()
) {
    AddEditTeamScreen(
        onPopBackStack = onPopBackStack,
        addEditTeamState = viewModel.state.value,
        //uiEvent = uiEvent.value,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTeamScreen(
    onPopBackStack: () -> Unit,
    addEditTeamState: AddEditTeamState,
    //uiEvent: UiEvent?,
    onEvent: (AddEditTeamEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (addEditTeamState.showEditTeamNameDialog) {
        AddEditTeamDialog(
            onDismissRequest = { onEvent(AddEditTeamEvent.OnHideEditTeamNameDialog) },
            onConfirmRequest = { onEvent(AddEditTeamEvent.OnProcessSaveTeam(it)) },
            errorMessage = addEditTeamState.saveError,
            initialName = addEditTeamState.team?.name
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                            text = addEditTeamState.team?.name ?: "",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column(
                        modifier = Modifier.weight(.16f),
                        horizontalAlignment = Alignment.End
                    ) {
                        IconButton(
                            onClick = { onEvent(AddEditTeamEvent.OnShowEditTeamNameDialog) }
                        ) {
                            Icon(Icons.Default.Edit, "Edit team button")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = addEditTeamState.playersText,
                    onValueChange = {
                        onEvent(AddEditTeamEvent.OnPlayersChanged(it))
                    },
                    maxLines = 1,
                    placeholder = {
                        Text(text = "Jersey Number")
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onEvent(AddEditTeamEvent.OnPlayersChangedDone)
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onKeyEvent { event ->
                            when (event.key) {
                                Key.Enter -> {
                                    onEvent(AddEditTeamEvent.OnPlayersChangedDone)
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
                        items(addEditTeamState.players.size) { index ->
                            PlayerItem(
                                player = addEditTeamState.players[index],
                                onDeleteClick = {
                                    onEvent(
                                        AddEditTeamEvent.OnDeletePlayerClick(
                                            addEditTeamState.players[index]
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
    AddEditTeamScreen(
        onPopBackStack = { },
        addEditTeamState = AddEditTeamState(
            team = Team(1, "Knights"),
            players = listOf(
                Player(1, 1, "10"),
                Player(1, 1, "23")
            )
        ),
        //uiEvent = null,
        onEvent = {}
    )
}
