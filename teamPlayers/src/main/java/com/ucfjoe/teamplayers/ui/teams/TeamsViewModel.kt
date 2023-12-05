package com.ucfjoe.teamplayers.ui.teams

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.Screen
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
class TeamsViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _state = mutableStateOf(TeamsState())
    val state: State<TeamsState> = _state

    init {
        loadTeams()
    }

    private fun loadTeams() {
        viewModelScope.launch() {
            teamRepository.getTeams().onEach { teams ->
                _state.value = state.value.copy(teams = teams)
            }.launchIn(viewModelScope)
        }
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: TeamsEvent) {
        when (event) {
            is TeamsEvent.OnAddTeamClick -> {
                sendUiEvent(UiEvent.Navigate(Screen.AddEditTeamScreen.route))
            }

            is TeamsEvent.OnTeamClick -> {
                val screen =
                    if (state.value.isEditMode) Screen.AddEditTeamScreen else Screen.TeamDetailsScreen
                sendUiEvent(UiEvent.Navigate(screen.route + "?team_id=${event.team.id}"))
            }

            is TeamsEvent.OnToggleEditMode -> {
                _state.value = state.value.copy(isEditMode = !state.value.isEditMode)
            }

            is TeamsEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    teamRepository.deleteTeam(event.team)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}