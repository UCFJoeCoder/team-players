package com.ucfjoe.teamplayers.ui.team_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.Screen
import com.ucfjoe.teamplayers.domain.repository.GameRepository
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
class TeamDetailsViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val gameRepository: GameRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(TeamDetailsState())
    val state: State<TeamDetailsState> = _state

    private val _navEvent = Channel<NavEvent>()
    val navEvent = _navEvent.receiveAsFlow()

    init {
        val paramTeamId: String? = savedStateHandle["team_id"]
        paramTeamId?.toLong()?.let { teamId ->
            viewModelScope.launch {
                teamRepository.getTeamWithGames(teamId).onEach {
                    _state.value = state.value.copy(team = it.team, games = it.games.sorted())
                }.launchIn(viewModelScope)
            }
        }
    }

    fun onEvent(event: TeamDetailsEvent) {
        when (event) {
            is TeamDetailsEvent.OnAddGameClick -> {
                sendNavEvent(NavEvent.Navigate(Screen.AddEditGameScreen.route + "?team_id=${event.teamId}"))
            }

            is TeamDetailsEvent.OnGameClick -> {
                val screen =
                    if (state.value.isEditMode) Screen.AddEditGameScreen else Screen.GameDetailsScreen
                sendNavEvent(NavEvent.Navigate(screen.route + "?team_id=${event.game.teamId}&game_id=${event.game.id}"))
            }

            is TeamDetailsEvent.OnToggleEditMode -> {
                _state.value = state.value.copy(isEditMode = !state.value.isEditMode)
            }

            is TeamDetailsEvent.OnDeleteGameClick -> {
                viewModelScope.launch {
                    gameRepository.deleteGame(event.game)
                }
            }

            is TeamDetailsEvent.OnEditTeamClick -> {
                sendNavEvent(NavEvent.Navigate(Screen.AddEditTeamScreen.route + "?team_id=${state.value.team.id}"))
            }
        }
    }

    private fun sendNavEvent(event: NavEvent) {
        viewModelScope.launch {
            _navEvent.send(event)
        }
    }
}