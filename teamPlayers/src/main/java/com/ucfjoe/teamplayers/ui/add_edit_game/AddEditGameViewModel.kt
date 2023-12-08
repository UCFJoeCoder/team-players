package com.ucfjoe.teamplayers.ui.add_edit_game

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.repository.GameRepository
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import com.ucfjoe.teamplayers.ui.NavEvent
import com.ucfjoe.teamplayers.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddEditGameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val teamRepository: TeamRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(AddEditGameState())
    val state: State<AddEditGameState> = _state

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _navEvent = Channel<NavEvent>()
    val navEvent = _navEvent.receiveAsFlow()

    init {
        val paramTeamId: String? = savedStateHandle["team_id"]
        paramTeamId?.toLong()?.let { teamId ->
            viewModelScope.launch {
                teamRepository.getTeam(teamId)?.let { team ->
                    _state.value = state.value.copy(teamId = teamId, team = team)
                }
            }
        }

        val paramGameId: String? = savedStateHandle["game_id"]
        paramGameId?.toLong()?.let { gameId ->
            viewModelScope.launch {
                gameRepository.getGame(gameId)?.let { game ->
                    _state.value = state.value.copy(game = game)
                }
            }
        }
    }

    fun onEvent(event: AddEditGameEvent) {
        when (event) {
            is AddEditGameEvent.OnSaveGameClick -> {
                processSaveGameClick()
            }

            is AddEditGameEvent.OnDateChanged -> {
                _state.value = state.value.copy(date = event.date, showDatePicker = false)
            }

            is AddEditGameEvent.OnTimeChanged -> {
                _state.value = state.value.copy(time = event.time, showTimePicker = false)
            }

            is AddEditGameEvent.OnShowDatePicker -> {
                _state.value = state.value.copy(showDatePicker = true)
            }

            is AddEditGameEvent.OnHideDatePicker -> {
                _state.value = state.value.copy(showDatePicker = false)
            }

            is AddEditGameEvent.OnShowTimePicker -> {
                _state.value = state.value.copy(showTimePicker = true)
            }

            is AddEditGameEvent.OnHideTimePicker -> {
                _state.value = state.value.copy(showTimePicker = false)
            }
        }
    }

    private fun processSaveGameClick() {
        viewModelScope.launch {

            val game = Game(
                id = state.value.game?.id ?: 0,
                teamId = state.value.teamId,
                gameDateTime = LocalDateTime.of(state.value.date, state.value.time)
            )

            gameRepository.upsertGame(game)

            sendNavEvent(NavEvent.PopBackStack)
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun sendNavEvent(event: NavEvent) {
        viewModelScope.launch {
            _navEvent.send(event)
        }
    }
}