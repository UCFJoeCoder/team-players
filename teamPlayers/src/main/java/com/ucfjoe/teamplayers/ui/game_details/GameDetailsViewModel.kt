package com.ucfjoe.teamplayers.ui.game_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.use_case.GameDetailsUseCases
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
    private val gameDetailsUseCases: GameDetailsUseCases,
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
                gameDetailsUseCases.getTeamWithGamesUseCase(teamId).onEach {
                    _state.value = state.value.copy(team = it.team)
                }.launchIn(viewModelScope)
            }
        }

        val paramGameId: String? = savedStateHandle["game_id"]
        paramGameId?.toLong()?.let { gameId ->
            viewModelScope.launch {
                gameDetailsUseCases.getGameWithGamePlayersUseCase(gameId).onEach {
                    _state.value = state.value.copy(
                        game = it.game,
                        players = it.gamePlayers.sorted(),
                        showImportCurrentPlayerDialog = it.gamePlayers.isEmpty()
                    )
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    fun onEvent(event: GameDetailsEvent) {
        when (event) {
            GameDetailsEvent.OnImportPlayers -> {
                viewModelScope.launch {
                    gameDetailsUseCases.importPlayersIntoGamePlayersUseCase(
                        state.value.game!!.id,
                        state.value.game!!.teamId
                    )
                }
            }

            GameDetailsEvent.OnRequestImportPlayers -> {
                _state.value = state.value.copy(showImportCurrentPlayerDialog = true)
            }

            GameDetailsEvent.OnDismissImportDialog -> {
                _state.value = state.value.copy(showImportCurrentPlayerDialog = false)
            }

            GameDetailsEvent.OnResetCountsToZero -> {
                viewModelScope.launch {
                    gameDetailsUseCases.resetCountsToZeroUseCase(state.value.players)
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
                        it.copy(isSelected = !it.isAbsent && lastPlayersSelected.contains(it.id))
                    }
                )
            }

            GameDetailsEvent.OnIncrementSelectionClick -> {
                val selectedIds = state.value.players.filter { it.isSelected }.map { it.id }

                viewModelScope.launch {
                    when (val result = gameDetailsUseCases.incrementSelectedGamePlayersUseCase(state.value.players)){
                        is Resource.Success -> {
                            lastPlayersSelected.clear()
                            lastPlayersSelected.addAll(selectedIds)
                            _state.value = state.value.copy(players = result.data!!)
                        }
                        is Resource.Error -> {
                            sendUiEvent(UiEvent.ShowToast(message = result.message!!))
                        }
                    }
                }
            }

            is GameDetailsEvent.OnSelectPlayerClick -> {
                if (event.player.isAbsent) return
                _state.value = state.value.copy(
                    players = state.value.players.map { if (it == event.player) it.copy(isSelected = !it.isSelected) else it }
                )
            }

            is GameDetailsEvent.OnEditPlayerClick -> {
                _state.value = state.value.copy(
                    showEditPlayerDialog = true,
                    editPlayer = event.player,
                    editErrorMessage = null
                )
            }

            GameDetailsEvent.OnHideEditPlayerDialog -> {
                _state.value = state.value.copy(showEditPlayerDialog = false)
            }

            is GameDetailsEvent.OnProcessEditPlayerRequest -> {
                processSaveEditPlayer(event.editPlayer)
            }

            GameDetailsEvent.OnShowPopupMenu -> {
                _state.value = state.value.copy(showPopupMenu = true)
            }

            GameDetailsEvent.OnHidePopupMenu -> {
                _state.value = state.value.copy(showPopupMenu = false)
            }

            GameDetailsEvent.OnShowShareGameResultsDialog -> {
                _state.value = state.value.copy(showShareGameDetailsDialog = true)
            }

            GameDetailsEvent.OnHideShareGameResultsDialog -> {
                _state.value = state.value.copy(showShareGameDetailsDialog = false)
            }

            GameDetailsEvent.OnShowHelpDialog -> {
                _state.value = state.value.copy(showHelpDialog = true)
            }

            GameDetailsEvent.OnHideHelpDialog -> {
                _state.value = state.value.copy(showHelpDialog = false)
            }

            GameDetailsEvent.OnShowResetCountDialog -> {
                _state.value = state.value.copy(showRequestClearCountDialog = true)
            }

            GameDetailsEvent.OnHideResetCountDialog -> {
                _state.value = state.value.copy(showRequestClearCountDialog = false)
            }
        }
    }

    private fun processSaveEditPlayer(editPlayer: GamePlayer) {
        viewModelScope.launch {
            when (val result =
                gameDetailsUseCases.editGamePlayerUseCase(
                    editPlayer.id,
                    editPlayer.gameId,
                    editPlayer.jerseyNumber,
                    editPlayer.count,
                    editPlayer.isAbsent
                )) {
                is Resource.Success -> {
                    _state.value = state.value.copy(showEditPlayerDialog = false)
                }

                is Resource.Error -> {
                    _state.value = state.value.copy(editErrorMessage = result.message)
                }
            }
        }
    }
}