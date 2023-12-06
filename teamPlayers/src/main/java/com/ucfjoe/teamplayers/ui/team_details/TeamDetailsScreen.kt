package com.ucfjoe.teamplayers.ui.team_details

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucfjoe.teamplayers.R
import com.ucfjoe.teamplayers.ui.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TeamDetailsViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowSnackbar -> {
                    TODO("Show message on snackbar ${event.message}")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxWidth(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    Text(
                        text = "Game Time",
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
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TeamDetailsEvent.OnAddGameClick(viewModel.state.value.team.id))
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Game"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 25.dp)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.fillMaxWidth(.5f)) {
                        Text(
                            text = viewModel.state.value.team.name,
                            modifier = Modifier,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        // Only show edit toggle IconButton if we have games that can be edited
                        if (viewModel.state.value.games.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onEvent(TeamDetailsEvent.OnToggleEditMode) }) {
                                GetEditActionIcon(viewModel.state.value.isEditMode)
                            }
                        }
                    }
                }
                HorizontalDivider()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.state.value.games) { game ->
                        GameItem(
                            game = game,
                            onEvent = viewModel::onEvent,
                            isEditMode = viewModel.state.value.isEditMode
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
        Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel Edit Mode")
    } else {
        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Mode")
    }
}
