package com.ucfjoe.teamplayers.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.Screen
import com.ucfjoe.teamplayers.repository.TeamRepository
import com.ucfjoe.teamplayers.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    private val repository: TeamRepository
): ViewModel() {

    val teams = repository.getTeams()

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: TeamsEvent) {
        when(event){
            is TeamsEvent.OnTeamClick -> {
                sendUiEvent(UiEvent.Navigate(Screen.CreateEditTeamScreen.route + "?team_id=${event.team.id}"))
            }
            is TeamsEvent.OnAddTeamClick -> {
                sendUiEvent(UiEvent.Navigate(Screen.CreateEditTeamScreen.route))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}