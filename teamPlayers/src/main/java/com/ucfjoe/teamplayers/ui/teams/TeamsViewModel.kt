package com.ucfjoe.teamplayers.ui.teams

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.Screen
import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.use_case.TeamsUseCases
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
    private val teamsUseCases: TeamsUseCases
) : ViewModel() {

    private val _state = mutableStateOf(TeamsState())
    val state: State<TeamsState> = _state

    private val _navEvent = Channel<NavEvent>()
    val navEvent = _navEvent.receiveAsFlow()

    init {
        viewModelScope.launch() {
            teamsUseCases.getTeamsUseCase().onEach { teams ->
                _state.value = state.value.copy(teams = teams)
            }.launchIn(viewModelScope)
        }
    }

    fun onEvent(event: TeamsEvent) {
        when (event) {
            is TeamsEvent.OnAddTeamClick -> {
                _state.value =
                    state.value.copy(showAddTeamDialog = true, addTeamErrorMessage = null)
            }

            is TeamsEvent.OnHideAddTeamDialog -> {
                _state.value = state.value.copy(showAddTeamDialog = false)
            }

            is TeamsEvent.OnProcessAddTeamRequest -> {
                processAddTeamRequest(event.name)
            }

            is TeamsEvent.OnTeamClick -> {
                val screen =
                    if (state.value.isEditMode) Screen.EditTeamScreen else Screen.TeamDetailsScreen
                sendNavEvent(NavEvent.Navigate(screen.route + "?team_id=${event.team.id}"))
            }

            is TeamsEvent.OnToggleEditMode -> {
                _state.value = state.value.copy(isEditMode = !state.value.isEditMode)
            }

            is TeamsEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    teamsUseCases.deleteTeamUseCase(event.team)
                }
            }
        }
    }

    private fun processAddTeamRequest(name: String) {
        viewModelScope.launch {
            when (val result = teamsUseCases.addTeamUseCase(name)) {
                is Resource.Success -> {
                    _state.value = state.value.copy(showAddTeamDialog = false)
                    sendNavEvent(NavEvent.Navigate(route = Screen.EditTeamScreen.route + "?team_id=${result.data.id}"))
                }

                is Resource.Error -> {
                    _state.value = state.value.copy(addTeamErrorMessage = result.message)
                }
            }
        }
    }

    private fun sendNavEvent(event: NavEvent) {
        viewModelScope.launch {
            _navEvent.send(event)
        }
    }
}