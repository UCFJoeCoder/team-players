package com.ucfjoe.teamplayers.ui.add_edit_team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ucfjoe.teamplayers.Screen
import com.ucfjoe.teamplayers.ui.UiEvent
import com.ucfjoe.teamplayers.ui.teams.TeamsEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTeamScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTeamViewModel = hiltViewModel()
    //navController: NavController,
    //teamIdString: String?
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
                viewModel.onEvent(AddEditTeamEvent.OnSaveTeamClick)
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
        ) {
            TextField(
                value = viewModel.name,
                onValueChange = {
                    viewModel.onEvent(AddEditTeamEvent.OnNameChanged(it))
                },
                placeholder = {
                    Text(text = "Team Name")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = viewModel.players,
                onValueChange = {
                    viewModel.onEvent(AddEditTeamEvent.OnPlayersChanged(it))
                },
                placeholder = {
                    Text(text = "Comma Separated list of Jersey Numbers")
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
//
//fun getTextString(teamId: String?): String {
//    return if (teamId==null) {
//        "New Team"
//    }
//    else {
//        "Edit Team $teamId"
//    }
//}
//
//private fun isLong(str: String): Boolean {
//    return try {
//        str.toLong()
//        true
//    } catch (e: NumberFormatException) {
//        false
//    }
//}
//
//fun String.toLong(): Long? {
//    return try{
//        toLong()
//    } catch (e: NumberFormatException) {
//        null
//    }
//}

@Preview(showBackground = true)
@Composable
fun PreviewAddEditTeamScreen() {
    AddEditTeamScreen(onPopBackStack = {})
}
