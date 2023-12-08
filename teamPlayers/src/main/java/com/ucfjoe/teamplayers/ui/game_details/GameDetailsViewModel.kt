package com.ucfjoe.teamplayers.ui.game_details

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.domain.repository.GamePlayerRepository
import com.ucfjoe.teamplayers.domain.repository.GameRepository
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
class GameDetailsViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val gameRepository: GameRepository,
    private val gamePlayerRepository: GamePlayerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = mutableStateOf(GameDetailsState())
    val state: State<GameDetailsState> = _state

    private val lastPlayersSelected = mutableListOf<Long>()

    init {
        val paramTeamId: String? = savedStateHandle["team_id"]
        paramTeamId?.toLong()?.let { teamId ->
            viewModelScope.launch {
                teamRepository.getTeamWithGames(teamId).onEach {
                    _state.value = state.value.copy(team = it.team)
                }.launchIn(viewModelScope)
            }
        }

        val paramGameId: String? = savedStateHandle["game_id"]
        paramGameId?.toLong()?.let { gameId ->
            viewModelScope.launch {
                gameRepository.getGameWithGamePlayers(gameId).onEach {
                    _state.value = state.value.copy(
                        game = it.game,
                        players = it.gamePlayers.sorted(),
                        askToImportCurrentPlayer = it.gamePlayers.isEmpty()
                    )
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            Log.e("asdf", "_uiEvent.send(event)")
            _uiEvent.send(event)
        }
    }

    fun onEvent(event: GameDetailsEvent) {
        when (event) {
            GameDetailsEvent.OnImportPlayers -> {
                viewModelScope.launch {
                    gamePlayerRepository.insertGamePlayersFromTeamPlayers(
                        state.value.game!!.id,
                        state.value.game!!.teamId
                    )
                }
            }

            GameDetailsEvent.OnRequestImportPlayers -> {
                _state.value = state.value.copy(askToImportCurrentPlayer = true)
            }

            GameDetailsEvent.OnDismissImportDialog -> {
                _state.value = state.value.copy(askToImportCurrentPlayer = false)
            }

            GameDetailsEvent.OnResetCountsToZero -> {
                _state.value = state.value.copy(
                    players = state.value.players.map { it.copy(count = 0) }
                )
                // Store the changes that were just made locally to the database
                viewModelScope.launch {
                    gamePlayerRepository.upsertGamePlayer(state.value.players)
                }
            }

            GameDetailsEvent.OnCancelSelectionClick -> {
                _state.value = state.value.copy(
                    players = state.value.players.map { it.copy(isSelected = false) }
                )
            }

            GameDetailsEvent.OnRepeatSelectionClick -> {
                _state.value = state.value.copy(
                    players = state.value.players.map {
                        it.copy(isSelected = lastPlayersSelected.contains(it.id))
                    }
                )
            }

            GameDetailsEvent.OnIncrementSelectionClick -> {
                if (state.value.players.count { it.isSelected } == 0) {
                    Log.e("asdf", "SendUIEvent")
                    sendUiEvent(UiEvent.ShowToast("No players are selected!"))
                    return
                }

                lastPlayersSelected.clear()

                // Save a list of the currently selected players. This is used by the OnRepeatSelection
                state.value.players.filter { it.isSelected }.map { it.id }
                    .toCollection(lastPlayersSelected)

                // Update the Count and isSelected state
                _state.value = state.value.copy(
                    players = state.value.players.map { gamePlayer ->
                        if (gamePlayer.isSelected)
                            gamePlayer.copy(count = gamePlayer.count + 1, isSelected = false)
                        else
                            gamePlayer
                    }
                )

                // Store the changes that were just made locally to the database
                viewModelScope.launch {
                    gamePlayerRepository.upsertGamePlayer(state.value.players)
                }
            }

            is GameDetailsEvent.OnSelectPlayerClick -> {
                _state.value = state.value.copy(
                    players = state.value.players.map { if (it == event.player) it.copy(isSelected = it.isSelected.not()) else it }
                )
            }

            GameDetailsEvent.OnShowPopupMenu -> {
                _state.value = state.value.copy(showPopupMenu = true)
            }

            GameDetailsEvent.OnHidePopupMenu -> {
                _state.value = state.value.copy(showPopupMenu = false)
            }

            GameDetailsEvent.OnShareGameResults -> {
                _state.value = state.value.copy(showShareGameDetailsDialog = true)
            }

            GameDetailsEvent.OnHideShareGameResults -> {
                _state.value = state.value.copy(showShareGameDetailsDialog = false)
            }

            GameDetailsEvent.OnShowHelpDialog -> {
                _state.value = state.value.copy(showHelpDialog = true)
            }

            GameDetailsEvent.OnHideHelpDialog -> {
                _state.value = state.value.copy(showHelpDialog = false)
            }

            GameDetailsEvent.OnShowRequestClearCountDialog -> {
                _state.value = state.value.copy(showRequestClearCountDialog = true)
            }

            GameDetailsEvent.OnHideRequestClearCountDialog -> {
                _state.value = state.value.copy(showRequestClearCountDialog = false)
            }
        }
    }
}