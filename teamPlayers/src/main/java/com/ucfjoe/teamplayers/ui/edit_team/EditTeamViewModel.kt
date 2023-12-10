package com.ucfjoe.teamplayers.ui.edit_team

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.use_case.EditTeamUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTeamViewModel @Inject constructor(
    private val editTeamUseCases: EditTeamUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(EditTeamState())
    val state: State<EditTeamState> = _state

    init {
        val paramTeamId: String? = savedStateHandle["team_id"]
        paramTeamId?.toLong()?.let { teamId ->
            viewModelScope.launch {
                editTeamUseCases.getTeamWithPlayersUseCase(teamId).onEach {
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
                _state.value =
                    state.value.copy(playersText = event.playersText, savePlayerError = null)

                // Auto process a jersey number once the number of characters typed is 2
                if (event.playersText.length == 2) {
                    processJerseyNumber()
                }
            }

            is EditTeamEvent.OnPlayersChangedDone -> {
                // User pressed the Done button so process current playersText
                processJerseyNumber()
            }

            is EditTeamEvent.OnDeletePlayerClick -> {
                viewModelScope.launch {
                    // Should we display a confirmation dialog? I kind of like that the player just
                    // deletes. They are easy to create again... if deleted in error.
                    editTeamUseCases.deletePlayerUseCase(event.player)
                }
            }

            is EditTeamEvent.OnProcessSaveTeam -> {
                processOnSaveTeamClick(event.name)
            }

            EditTeamEvent.OnHideEditTeamNameDialog -> {
                _state.value = state.value.copy(showEditTeamNameDialog = false)
            }

            EditTeamEvent.OnShowEditTeamNameDialog -> {
                _state.value = state.value.copy(showEditTeamNameDialog = true, saveTeamError = null)
            }
        }
    }

    private fun processJerseyNumber() {
        viewModelScope.launch {
            when (val result = editTeamUseCases.addPlayerToTeamUseCase(
                state.value.playersText,
                state.value.team!!.id
            )) {
                is Resource.Success -> {
                    // Player added successfully, clear the text
                    _state.value = state.value.copy(playersText = "")
                }

                is Resource.Error -> {
                    // Display error and wipe the text that caused the error
                    _state.value =
                        state.value.copy(savePlayerError = result.message, playersText = "")
                }
            }
        }
    }

    private fun processOnSaveTeamClick(name: String) {
        viewModelScope.launch {
            val newName = name.trim()
            val team = state.value.team!!

            when (val result = editTeamUseCases.editTeamUseCase(newName, team.name, team.id)) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        team = team.copy(name = newName),
                        showEditTeamNameDialog = false
                    )
                }

                is Resource.Error -> {
                    _state.value = state.value.copy(saveTeamError = result.message)

                }
            }
        }
    }
}
