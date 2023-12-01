package com.ucfjoe.teamplayers.ui.add_edit_team

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.ui.UiEvent
import com.ucfjoe.teamplayers.ui.theme.teamPlayerButtonColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTeamScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: AddEditTeamViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
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
                //else -> Unit
            }
        }
    }

    Box(modifier = Modifier)
    {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.green_grass_field),
            contentDescription = "green grass",
            contentScale = ContentScale.Crop
        )
        Scaffold(
            containerColor = Color.Transparent,
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
                    text = getScreenTitle(viewModel.state.value.editMode),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium
                        .copy(
                            fontFamily = FontFamily(Font(R.font.old_sport_college)),
                            color = Color.White
                        ),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    TextField(
                        value = viewModel.state.value.nameText,
                        onValueChange = {
                            viewModel.onEvent(AddEditTeamEvent.OnNameChanged(it))
                        },
                        label = {
                            Text(text = "Team Name")
                        },
                        placeholder = {
                            Text(text = "Team Name")
                        },
                        modifier = Modifier.fillMaxWidth(.70f)
                    )
                    Button(
                        onClick = { viewModel.onEvent(AddEditTeamEvent.OnSaveTeamClick) },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(.30f)
                            .padding(5.dp),
                        colors = teamPlayerButtonColors(),
                        enabled = viewModel.state.value.enableSave,
                        border = BorderStroke(2.dp, color = Color.White)
                    ) {
                        Text(
                            text = (if (viewModel.state.value.editMode) "Save" else "Add"),
                            style = MaterialTheme.typography.labelLarge
                                .copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                        )
                    }
                }

                if (viewModel.state.value.editMode) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = viewModel.state.value.playersText,
                        onValueChange = {
                            viewModel.onEvent(AddEditTeamEvent.OnPlayersChanged(it))
                        },
                        label = {
                            Text(text = "Jersey Number")
                        },
                        placeholder = {
                            Text(text = "Type New Jersey Number")
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

fun getScreenTitle(isEditMode: Boolean): String {
    return if (isEditMode) "Edit Team" else "Add Team"
}


// TODO("See what can be done to provided a preview")
@Preview(showBackground = true)
@Composable
fun PreviewAddEditTeamScreen() {
    AddEditTeamScreen(onPopBackStack = {}, onNavigate = {})
}
