package com.ucfjoe.teamplayers.ui.add_edit_team

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.Screen
import com.ucfjoe.teamplayers.domain.model.Player
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.repository.PlayerRepository
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
class AddEditTeamViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val playerRepository: PlayerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(AddEditTeamState())
    val state: State<AddEditTeamState> = _state

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val paramTeamId: String? = savedStateHandle["team_id"]
        paramTeamId?.toLong()?.let { teamId ->
            viewModelScope.launch {
                teamRepository.getTeam(teamId)?.let { team ->
                    // Store the team in the View Model
                    _state.value = state.value.copy(
                        team = team,
                        nameText = team.name
                    )
                }
                playerRepository.getTeamPlayers(teamId).onEach { players ->
                    _state.value = state.value.copy(players = sortPlayers(players))
                }.launchIn(viewModelScope)
            }
        }
    }

    fun onEvent(event: AddEditTeamEvent) {
        when (event) {
            is AddEditTeamEvent.OnNameChanged -> {
                _state.value = state.value.copy(
                    nameText = event.name
                )
            }

            is AddEditTeamEvent.OnPlayersChanged -> {
                val playersText = event.players
                _state.value = state.value.copy(playersText = playersText)

                // Removed check for editMode since the onPlayersChanged shouldn't be visible unless in edit mode
                if (playersText.length == 2) {
                    processJerseyNumber()
                }
            }

            is AddEditTeamEvent.OnPlayersChangedDone -> {
                // Can this even get triggered if you are not in isEditMode? I think not
                //if (isEditMode()) {
                processJerseyNumber()
                //}
            }

            is AddEditTeamEvent.OnSaveTeamClick -> {
                processOnSaveTeamClick()
            }

            is AddEditTeamEvent.OnDeleteTeamClick -> {
                viewModelScope.launch {
                    // TODO("Display dialog confirming delete team")
                    sendUiEvent(UiEvent.ShowSnackbar("onDelete", "onDeleteAction"))
                }
            }

            is AddEditTeamEvent.OnDeletePlayerClick -> {
                viewModelScope.launch {
                    // TODO("Display dialog confirming delete player")
                    playerRepository.deletePlayer(event.player)
                }
            }
        }
    }

    private fun processJerseyNumber() {
        val jerseyNumber = getJerseyFromString(state.value.playersText)
        if (jerseyNumber != null) {
            viewModelScope.launch {
                addNewPlayer(state.value.team!!.id, jerseyNumber)
            }
        } else {
            Log.w("JOEY", "Failed to get valid jersey Number")
        }
        // clear the text after this action
        _state.value = state.value.copy(playersText = "")
    }

    private fun sortPlayers(player: List<Player>): List<Player> {
        // Create a numeric comparator that allows for 2 to come before 10.
        // i.e. 1,10,2,20 would be sorted as 1,2,10,20
        val comparatorInt = Comparator { p1: Player, p2: Player ->
            p1.jerseyNumber.toInt() - p2.jerseyNumber.toInt()
        }
        val comparatorStr = Comparator { p1: Player, p2: Player ->
            p1.jerseyNumber.compareTo(p2.jerseyNumber)
        }

        // create a list of only numbers sorted with the comparator
        val list1 =
            player.filter { p -> p.jerseyNumber.toIntOrNull() != null }.sortedWith(comparatorInt)
        // create a list of non-numbers sorted with the default sort
        val list2 =
            player.filter { p -> p.jerseyNumber.toIntOrNull() == null }.sortedWith(comparatorStr)

        return list1 + list2
    }

    private fun processOnSaveTeamClick() {
        viewModelScope.launch {
            // Make sure the data is valid before upsert
            if (state.value.nameText.isBlank()) {
                sendUiMessage("The name cannot be empty")
                return@launch
            }
            // Check for a duplicate Team name
            val count = teamRepository.getTeamsWithName(state.value.nameText.trim())
            if (count > 0) {
                sendUiMessage("Duplicate Name")
                return@launch
            }
            // Insert or update the entity in repository.
            val id = teamRepository.upsertTeam(
                Team(
                    name = state.value.nameText,
                    id = state.value.team?.id ?: 0
                )
            )
            // If we are not already in edit mode then reload this Composable in edit more with
            // just created teamId
            //if (!isEditMode()) {
            if (!state.value.editMode) {
                sendUiEvent(UiEvent.PopBackStack)
                sendUiEvent(UiEvent.Navigate(Screen.CreateEditTeamScreen.route + "?team_id=${id}"))
            }
        }
    }

    private fun sendUiMessage(message: String) {
        sendUiEvent(UiEvent.ShowSnackbar(message))
    }

    private suspend fun addNewPlayer(teamId: Long, jerseyNumber: String) {
        val p = Player(
            teamId = teamId,
            jerseyNumber = jerseyNumber,
            id = 0
        )
        playerRepository.upsertPlayer(p)
        Log.d("JOEY", "Upsert Player $p")
    }

    private fun getJerseyFromString(data: String): String? {
        if (data.isNotBlank() && data.trim().length <= 2 && data.trim()
                .matches("^[a-zA-Z0-9]*$".toRegex())
        ) {
            val jerseyNumber = data.trim()
            if (state.value.players.find { it.jerseyNumber == jerseyNumber } == null) {
                return jerseyNumber
            } else {
                Log.d("JOEY", "'$data' already exists in the table so it is ignored")
            }
        } else {
            Log.w("JOEY", "'$data' fails length and regex test.")
        }
        return null
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}


