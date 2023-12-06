package com.ucfjoe.teamplayers.ui.teams

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.ui.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TeamsViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModelState = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    // TODO("remove if this is not going to be used")
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }

                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    Box()
    {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.football_field),
            contentDescription = "football field",
            contentScale = ContentScale.Crop
        )
        Scaffold(
            containerColor = Color.Transparent,
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

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
            )
            {
                Text(
                    text = "Team Players",
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
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.fillMaxWidth(.5f)) {
                        Text(
                            text = "Teams",
                            modifier = Modifier,
                            style = MaterialTheme.typography.titleLarge
                                .copy(color = Color.White)
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        // Only show edit toggle IconButton if we have teams that can be edited
                        if (viewModelState.teams.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onEvent(TeamsEvent.OnToggleEditMode) }) {
                                GetEditActionIcon(viewModelState.isEditMode)
                            }
                        }
                    }
                }

                HorizontalDivider()
                LazyColumn(
                    contentPadding = padding,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModelState.teams) { team ->
                        TeamItem(
                            team = team,
                            onEvent = viewModel::onEvent,
                            isEditMode = viewModelState.isEditMode,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GetEditActionIcon(isEditMode: Boolean) {
    if (isEditMode) {
        Icon(
            Icons.Default.Close, "Cancel Edit Mode",
            tint = com.ucfjoe.teamplayers.ui.theme.joe_theme_main_primary
        )
    } else {
        Icon(
            Icons.Default.Edit, "Edit Mode",
            tint = com.ucfjoe.teamplayers.ui.theme.joe_theme_main_primary
        )
    }
}

// TODO() try to get the preview to work with dependency injection
@Preview(showBackground = true)
@Composable
fun TeamScreenPreview() {
    TeamsScreen(onNavigate = { })
}
