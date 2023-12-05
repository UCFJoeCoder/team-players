package com.ucfjoe.teamplayers.ui.game_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.domain.repository.GamePlayerRepository
import com.ucfjoe.teamplayers.domain.repository.GameRepository
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val gameRepository: GameRepository,
    private val gamePlayerRepository: GamePlayerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

//    private val _uiEvent = Channel<UiEvent>()
//    val uiEvent = _uiEvent.receiveAsFlow()

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
                        players = it.gamePlayers,
                        askToImportCurrentPlayer = it.gamePlayers.isEmpty()
                    )
                }.launchIn(viewModelScope)
            }
        }
    }

    fun onEvent(event: GameDetailsEvent) {
        when (event) {
            GameDetailsEvent.OnImportPlayersRequest -> {
                _state.value = state.value.copy(askToImportCurrentPlayer = false)
                viewModelScope.launch {
                    gamePlayerRepository.insertGamePlayersFromTeamPlayers(
                        state.value.game!!.id,
                        state.value.game!!.teamId
                    )
                }
            }

            GameDetailsEvent.OnDismissImportDialog -> {
                _state.value = state.value.copy(askToImportCurrentPlayer = false)
            }

            GameDetailsEvent.OnResetCountsToZeroRequest -> {
                _state.value = state.value.copy(
                    players = state.value.players.map { it.copy(count = 0) }
                )
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

                // TODO("Save changes to players to the database")
            }

            is GameDetailsEvent.OnSelectPlayerClick -> {
                _state.value = state.value.copy(
                    players = state.value.players.map { if (it == event.player) it.copy(isSelected = !it.isSelected) else it }
                )
            }

            GameDetailsEvent.OnShowPopupMenu -> {
                _state.value = state.value.copy(showPopupMenu = true)
            }

            GameDetailsEvent.OnHidePopupMenu -> {
                _state.value = state.value.copy(showPopupMenu = false)
            }

            GameDetailsEvent.OnShareGameResults -> {
                println("TODO, Share the results of the game.")
            }
        }
    }
}