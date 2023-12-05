package com.ucfjoe.teamplayers.ui.add_edit_team

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.ui.UiEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddEditTeamScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: AddEditTeamViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()

                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }

                is UiEvent.Navigate -> onNavigate(event)
            }
        }
    }

    Box()
    {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 25.dp)
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
            ) {
                Text(
                    text = getScreenTitle(viewModel.state.value.isEditMode),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium
                        .copy(
                            fontFamily = FontFamily(Font(R.font.old_sport_college))
                        ),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    TextField(
                        modifier = Modifier.weight(.70f),
                        value = viewModel.state.value.nameText,
                        onValueChange = {
                            viewModel.onEvent(AddEditTeamEvent.OnNameChanged(it))
                        },
                        maxLines = 1,
                        placeholder = {
                            Text(text = "Team Name")
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Go,
                        ),
                        keyboardActions = KeyboardActions(
                            onGo = { viewModel.onEvent(AddEditTeamEvent.OnSaveTeamClick) }
                        ),
                        isError = viewModel.state.value.saveError != null,
                        supportingText = {
                            viewModel.state.value.saveError?.let {
                                Text(text=it)
                            }
                        }
                    )
                    Button(
                        modifier = Modifier
                            .weight(.30f)
                            .padding(5.dp),

                        onClick = {
                            keyboardController?.hide()
                            viewModel.onEvent(AddEditTeamEvent.OnSaveTeamClick)
                        },
                        enabled = viewModel.state.value.enableSave,
                    ) {
                        Icon(
                            getAddEditImageVector(viewModel.state.value.isEditMode),
                            contentDescription = "AddEditButton"
                        )
                    }
                }

                if (viewModel.state.value.isEditMode) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = viewModel.state.value.playersText,
                        onValueChange = {
                            viewModel.onEvent(AddEditTeamEvent.OnPlayersChanged(it))
                        },
                        maxLines = 1,
                        placeholder = {
                            Text(text = "Jersey Number")
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.onEvent(AddEditTeamEvent.OnPlayersChangedDone)
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onKeyEvent { event ->
                                when (event.key) {
                                    Key.Enter -> {
                                        viewModel.onEvent(AddEditTeamEvent.OnPlayersChangedDone)
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
                            items(viewModel.state.value.players.size) { index ->
                                PlayerItem(
                                    player = viewModel.state.value.players[index],
                                    onDeleteClick = {
                                        viewModel.onEvent(
                                            AddEditTeamEvent.OnDeletePlayerClick(
                                                viewModel.state.value.players[index]
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
}

fun getAddEditImageVector(isEditMode: Boolean): ImageVector {
    return if (isEditMode) Icons.Default.Edit else Icons.Default.Add
}

fun getScreenTitle(isEditMode: Boolean): String {
    return if (isEditMode) "Edit Team" else "Add Team"
}

// TODO("See what can be done to provided a preview")
@Preview(showBackground = true)
@Composable
fun PreviewAddEditTeamScreen() {
    AddEditTeamScreen(onPopBackStack = {}, onNavigate = {})
}
