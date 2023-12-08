package com.ucfjoe.teamplayers.ui.teams

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.Screen
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import com.ucfjoe.teamplayers.ui.NavEvent
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

    private val _navEvent = Channel<NavEvent>()
    val navEvent = _navEvent.receiveAsFlow()

    init {
        loadTeams()
    }

    fun onEvent(event: TeamsEvent) {
        when (event) {
            is TeamsEvent.OnAddTeamClick -> {
                _state.value = state.value.copy(showAddTeamDialog = true, addTeamErrorMessage = null)
            }

            is TeamsEvent.OnHideAddTeamDialog -> {
                _state.value = state.value.copy(showAddTeamDialog = false)
            }

            is TeamsEvent.OnProcessAddTeamRequest -> {
                processAddTeamRequest(event.name)
            }

            is TeamsEvent.OnTeamClick -> {
                Log.e("TeamsViewModel", "OnTeamClick")
                val screen =
                    if (state.value.isEditMode) Screen.AddEditTeamScreen else Screen.TeamDetailsScreen
                sendNavEvent(NavEvent.Navigate(screen.route + "?team_id=${event.team.id}"))
            }

            is TeamsEvent.OnToggleEditMode -> {
                _state.value = state.value.copy(isEditMode = state.value.isEditMode.not())
            }

            is TeamsEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    teamRepository.deleteTeam(event.team)
                }
            }
        }
    }

    private fun processAddTeamRequest(name: String) {
        viewModelScope.launch {
            // Make sure the data is valid before upsert
            if (name.isBlank()) {
                _state.value = state.value.copy(addTeamErrorMessage = "Name cannot be empty!")
                return@launch
            }

            // Perform case insensitive search
            val count = teamRepository.getTeamsWithName(name.trim())

            if (count > 0) {
                _state.value = state.value.copy(addTeamErrorMessage = "Duplicate name detected!")
                return@launch
            }
            // Insert or update the entity in repository.
            val upsertTeam = Team(name = name.trim())

            val id = teamRepository.upsertTeam(upsertTeam)

            _state.value = state.value.copy(showAddTeamDialog = false)

            sendNavEvent(NavEvent.Navigate(Screen.AddEditTeamScreen.route + "?team_id=${id}"))
        }
    }

    private fun loadTeams() {
        viewModelScope.launch() {
            teamRepository.getTeams().onEach { teams ->
                _state.value = state.value.copy(teams = teams)
            }.launchIn(viewModelScope)
        }
    }

    private fun sendNavEvent(event: NavEvent) {
        viewModelScope.launch {
            Log.e("sending", "sendNaveEvent")
            _navEvent.send(event)
        }
    }
}