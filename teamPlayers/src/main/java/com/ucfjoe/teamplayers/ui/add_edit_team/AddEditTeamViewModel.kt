package com.ucfjoe.teamplayers.ui.add_edit_team

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.Screen
import com.ucfjoe.teamplayers.domain.model.Player
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.repository.PlayerRepository
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import com.ucfjoe.teamplayers.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTeamViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val playerRepository: PlayerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(AddEditTeamState())
    val state: State<AddEditTeamState> = _state

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val paramTeamId: String? = savedStateHandle["team_id"]
        paramTeamId?.toLong()?.let { teamId ->
            viewModelScope.launch {
                // TODO(Wait for TeamDetails to implement the usage of the relation to pull data...)
                // TODO(Switch to relation, so only one database call is made. No need to pull players as a separate call)
                teamRepository.getTeam(teamId)?.let { team ->
                    // Store the team in the View Model
                    _state.value = state.value.copy(
                        team = team,
                        nameText = team.name
                    )
                }
                playerRepository.getTeamPlayers(teamId).onEach { players ->
                    _state.value = state.value.copy(players = players.sorted())
                }.launchIn(viewModelScope)
            }
        }
    }

    fun onEvent(event: AddEditTeamEvent) {
        when (event) {
            is AddEditTeamEvent.OnNameChanged -> {
                _state.value = state.value.copy(
                    nameText = event.name,
                    saveError = null
                )
            }

            is AddEditTeamEvent.OnPlayersChanged -> {
                val playersText = event.players
                _state.value = state.value.copy(playersText = playersText)

                // Removed check for editMode since the onPlayersChanged shouldn't be visible unless in edit mode
                if (playersText.length == 2) {
                    processJerseyNumber()
                }
            }

            is AddEditTeamEvent.OnPlayersChangedDone -> {
                processJerseyNumber()
            }

            is AddEditTeamEvent.OnSaveTeamClick -> {
                processOnSaveTeamClick()
            }

            is AddEditTeamEvent.OnDeletePlayerClick -> {
                viewModelScope.launch {
                    // Should we display a confirmation dialog? I kind of like that the player just
                    // deletes. They are easy to create again... if deleted in error.
                    playerRepository.deletePlayer(event.player)
                }
            }
        }
    }

    private fun processJerseyNumber() {
        val jerseyNumber = getJerseyFromString(state.value.playersText)
        if (jerseyNumber != null) {
            viewModelScope.launch {
                addNewPlayer(state.value.team!!.id, jerseyNumber)
            }
        } else {
            Log.w("processJerseyNumber", "Failed to get valid jersey Number")
        }
        // clear the text after this action
        _state.value = state.value.copy(playersText = "")
    }

    private fun processOnSaveTeamClick() {
        viewModelScope.launch {
            // Make sure the data is valid before upsert
            if (state.value.nameText.isBlank()) {
                _state.value = state.value.copy(saveError = "Name cannot be empty")
                return@launch
            }
            // Check for a duplicate Team name. If Editing then perform Case Sensitive search
            val count = if (state.value.isEditMode)
                teamRepository.getTeamsWithNameCaseSensitive(state.value.nameText.trim())
            else // If not editing, (aka. we are inserting), then perform case insensitive search
                teamRepository.getTeamsWithName(state.value.nameText.trim())

            if (count > 0) {
                _state.value = state.value.copy(saveError = "Duplicate name detected.")
                return@launch
            }
            // Insert or update the entity in repository.
            val upsertTeam = Team(
                name = state.value.nameText.trim(),
                id = state.value.team?.id ?: 0
            )
            val id = teamRepository.upsertTeam(upsertTeam)

            // If we just inserted then reload this Composable in edit mode with new teamId
            if (!state.value.isEditMode) {
                sendUiEvent(UiEvent.PopBackStack)
                sendUiEvent(UiEvent.Navigate(Screen.AddEditTeamScreen.route + "?team_id=${id}"))
            } else {
                // At this point the team stored in the state is out of sync with the database
                // So, I am updating the state with the upsertTeam
                _state.value = state.value.copy(team = upsertTeam, nameText = upsertTeam.name)
            }
        }
    }

//    private fun sendUiMessage(message: String) {
//        sendUiEvent(UiEvent.ShowSnackbar(message))
//    }

    private suspend fun addNewPlayer(teamId: Long, jerseyNumber: String) {
        val p = Player(
            teamId = teamId,
            jerseyNumber = jerseyNumber,
            id = 0
        )
        playerRepository.upsertPlayer(p)
    }

    private fun getJerseyFromString(data: String): String? {
        if (data.isNotBlank() && data.trim().length <= 2 && data.trim()
                .matches("^[a-zA-Z0-9]*$".toRegex())
        ) {
            val jerseyNumber = data.trim()
            if (state.value.players.find { it.jerseyNumber == jerseyNumber } == null) {
                return jerseyNumber
            } else {
                Log.d("getJerseyFromString", "'$data' already exists in the table so it is ignored")
            }
        } else {
            Log.w("getJerseyFromString", "'$data' fails length and regex test.")
        }
        return null
    }

        private fun sendUiEvent(event: UiEvent) {
            viewModelScope.launch {
                _uiEvent.send(event)
            }
        }
}


