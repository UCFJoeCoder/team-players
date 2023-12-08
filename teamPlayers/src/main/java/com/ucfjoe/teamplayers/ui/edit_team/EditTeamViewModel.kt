package com.ucfjoe.teamplayers.ui.edit_team

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.domain.model.Player
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.repository.PlayerRepository
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTeamViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val playerRepository: PlayerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(EditTeamState())
    val state: State<EditTeamState> = _state

    init {
        val paramTeamId: String? = savedStateHandle["team_id"]
        paramTeamId?.toLong()?.let { teamId ->
            viewModelScope.launch {
                teamRepository.getTeamWithPlayers(teamId).onEach {
                    _state.value = state.value.copy(
                        team = it.team,
                        players = it.players.sorted()
                    )
                }.launchIn(viewModelScope)
            }
        }
    }

    fun onEvent(event: EditTeamEvent) {
        when (event) {
            is EditTeamEvent.OnPlayersChanged -> {
                val playersText = event.players
                _state.value = state.value.copy(playersText = playersText)

                // Removed check for editMode since the onPlayersChanged shouldn't be visible unless in edit mode
                if (playersText.length == 2) {
                    processJerseyNumber()
                }
            }

            is EditTeamEvent.OnPlayersChangedDone -> {
                processJerseyNumber()
            }

            is EditTeamEvent.OnDeletePlayerClick -> {
                viewModelScope.launch {
                    // Should we display a confirmation dialog? I kind of like that the player just
                    // deletes. They are easy to create again... if deleted in error.
                    playerRepository.deletePlayer(event.player)
                }
            }

            is EditTeamEvent.OnProcessSaveTeam -> {
                processOnSaveTeamClick(event.name)
            }

            EditTeamEvent.OnHideEditTeamNameDialog -> {
                _state.value = state.value.copy(showEditTeamNameDialog = false)
            }

            EditTeamEvent.OnShowEditTeamNameDialog -> {
                _state.value = state.value.copy(showEditTeamNameDialog = true, saveError = null)
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

    private fun processOnSaveTeamClick(name: String) {
        viewModelScope.launch {

            // Make sure the data is valid before upsert
            if (name.isBlank()) {
                _state.value = state.value.copy(saveError = "Name cannot be empty!")
                return@launch
            }

            // Make sure the name has changed
            if (state.value.team!!.name == name.trim()) {
                // The name didn't change
                return@launch
            }

            // Check for a duplicate Team name. If Editing then perform Case Sensitive search
            val count = teamRepository.getTeamsWithNameCaseSensitive(name.trim())


            if (count > 0) {
                _state.value = state.value.copy(saveError = "Duplicate name detected!")
                return@launch
            }
            // Insert or update the entity in repository.
            val upsertTeam = Team(
                name = name.trim(),
                id = state.value.team?.id ?: 0
            )

            teamRepository.upsertTeam(upsertTeam)

            _state.value = state.value.copy(
                team = upsertTeam,
                showEditTeamNameDialog = false
            )
        }
    }

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
}
