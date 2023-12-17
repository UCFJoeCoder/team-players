package com.ucfjoe.teamplayers.ui.game_details

import android.net.Uri
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
    private val hasRunOnce = mutableStateOf(false)

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
                    )
                    if (!hasRunOnce.value) {
                        initialPlayersCheck()
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun initialPlayersCheck() {
        hasRunOnce.value = true
        if (state.value.players.isEmpty()) {
            // Auto import players from Team if the gamesPlayers list is empty
            processImportPlayers()
        } else {
            checkForPlayerChanges()
        }
    }

    private fun checkForPlayerChanges() {
        viewModelScope.launch {
            if (state.value.team == null) {
                return@launch
            }

            val differences = gameDetailsUseCases.getDifferencesBetweenPlayersAndGamePlayers(
                state.value.game!!.id,
                state.value.team!!.id
            )
            if (differences.isNotEmpty()) {
                _state.value = state.value.copy(
                    //changePlayersDetected = true,
                    showImportCurrentPlayerDialog = true
                )
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun processImportPlayers() {
        viewModelScope.launch {
            gameDetailsUseCases.importPlayersIntoGamePlayersUseCase(
                state.value.game!!.id,
                state.value.game!!.teamId
            )
        }
    }

    fun onEvent(event: GameDetailsEvent) {
        when (event) {
            GameDetailsEvent.OnImportPlayers -> {
                processImportPlayers()
            }

//            GameDetailsEvent.OnRequestImportPlayers -> {
//                _state.value = state.value.copy(
//                    changePlayersDetected = false,
//                    showImportCurrentPlayerDialog = true
//                )
//            }

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
                    when (val result =
                        gameDetailsUseCases.incrementSelectedGamePlayersUseCase(state.value.players)) {
                        is Resource.Success -> {
                            lastPlayersSelected.clear()
                            lastPlayersSelected.addAll(selectedIds)
                            _state.value = state.value.copy(players = result.data)
                        }

                        is Resource.Error -> {
                            sendUiEvent(UiEvent.ShowToast(message = result.message))
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

            GameDetailsEvent.OnDismissPopupMenu -> {
                _state.value = state.value.copy(showPopupMenu = false)
            }

            GameDetailsEvent.OnShowShareGameResultsDialog -> {
                _state.value = state.value.copy(showShareGameDetailsDialog = true)
            }

            GameDetailsEvent.OnDismissShareGameResultsDialog -> {
                _state.value = state.value.copy(showShareGameDetailsDialog = false)
            }

            GameDetailsEvent.OnShareGameData -> {
                shareGameData()
            }

            GameDetailsEvent.OnShowHelpDialog -> {
                _state.value = state.value.copy(showHelpDialog = true)
            }

            GameDetailsEvent.OnDismissHelpDialog -> {
                _state.value = state.value.copy(showHelpDialog = false)
            }

            GameDetailsEvent.OnShowResetCountDialog -> {
                _state.value = state.value.copy(showRequestClearCountDialog = true)
            }

            GameDetailsEvent.OnDismissResetCountDialog -> {
                _state.value = state.value.copy(showRequestClearCountDialog = false)
            }

            GameDetailsEvent.OnShowCompleteGameDialog -> {
                _state.value = state.value.copy(showCompleteGameDialog = true)
            }

            GameDetailsEvent.OnDismissCompleteGameDialog -> {
                _state.value = state.value.copy(showCompleteGameDialog = false)
            }

            is GameDetailsEvent.OnChangeGameCompletedState -> {
                processSetGameCompletedState(event.isCompleted)
            }

            GameDetailsEvent.OnStartActivityError -> {
                _state.value =
                    state.value.copy(
                        emailErrorMessage = "Failed to send email. Either there is " +
                                "no Email App on this device or another error occurred."
                    )
            }
        }
    }

    private fun shareGameData() {
        // TODO Determine if the game needs to be in a completed state or should be switched to a completed state with this action
        // Should this only be allowed when the game is completed?
        // Should this auto set the game as completed?
        // Should this tell the user, in a dialog, that by continuing this action will mark the game as completed?

        val team = state.value.team
        val game = state.value.game
        val players = state.value.players
        if (team == null || game == null || players.isEmpty()) {
            return
        }

        var uri = ""
        viewModelScope.launch {
            when (val result =
                gameDetailsUseCases.writeFileRepository.writeFile(team, game, players)) {
                is Resource.Success -> {
                    println("Joey Uri ${result.data}")
                    uri = result.data
                }

                is Resource.Error -> {
                    println("Joey ERROR ${result.message}")
                    _state.value = state.value.copy(emailErrorMessage = result.message)
                    return@launch
                }
            }
        }

        when (val result = gameDetailsUseCases.sendGameEmailUseCase(
            team.name,
            game.gameDateTime,
            Uri.parse(uri)
        )) {
            is Resource.Success -> Unit

            is Resource.Error -> {
                _state.value = state.value.copy(emailErrorMessage = result.message)
            }
        }
    }

    private fun processSetGameCompletedState(isCompleted: Boolean) {
        viewModelScope.launch {
            val game = state.value.game!!.copy(isCompleted = isCompleted)
            gameDetailsUseCases.updateGameUseCase(game)
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