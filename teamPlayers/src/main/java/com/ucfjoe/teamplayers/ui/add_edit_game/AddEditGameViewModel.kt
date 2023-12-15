package com.ucfjoe.teamplayers.ui.add_edit_game

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.domain.use_case.AddEditGameUseCases
import com.ucfjoe.teamplayers.ui.NavEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddEditGameViewModel @Inject constructor(
    private val addEditGameUseCases: AddEditGameUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(AddEditGameState())
    val state: State<AddEditGameState> = _state

    private val _navEvent = Channel<NavEvent>()
    val navEvent = _navEvent.receiveAsFlow()

    init {
        val paramTeamId: String? = savedStateHandle["team_id"]
        paramTeamId?.toLong()?.let { teamId ->
            viewModelScope.launch {
                addEditGameUseCases.getTeamByIdUseCase(teamId)?.let { team ->
                    _state.value = state.value.copy(teamId = teamId, team = team)
                }
            }
        }

        val paramGameId: String? = savedStateHandle["game_id"]
        paramGameId?.toLong()?.let { gameId ->
            viewModelScope.launch {
                addEditGameUseCases.getGameByIdUseCase(gameId)?.let { game ->
                    _state.value = state.value.copy(
                        game = game,
                        date = game.gameDateTime.toLocalDate(),
                        time = game.gameDateTime.toLocalTime()
                    )
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

            val newDateTime = LocalDateTime.of(state.value.date, state.value.time)
            addEditGameUseCases.upsertGameUseCase(newDateTime, state.value.teamId, state.value.game)

            // The above call only return Success.
            // There is no need to capture the results and verify
            sendNavEvent(NavEvent.PopBackStack)
        }
    }

    private fun sendNavEvent(event: NavEvent) {
        viewModelScope.launch {
            _navEvent.send(event)
        }
    }
}