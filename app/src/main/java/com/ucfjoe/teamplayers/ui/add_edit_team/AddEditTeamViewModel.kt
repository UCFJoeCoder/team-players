package com.ucfjoe.teamplayers.ui.add_edit_team

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.Screen
import com.ucfjoe.teamplayers.database.entity.Player
import com.ucfjoe.teamplayers.database.entity.Team
import com.ucfjoe.teamplayers.repository.PlayerRepository
import com.ucfjoe.teamplayers.repository.TeamRepository
import com.ucfjoe.teamplayers.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTeamViewModel @Inject constructor(
    private val repository: TeamRepository,
    private val playerRepo: PlayerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var team by mutableStateOf<Team?>(null)
        private set

    var players by mutableStateOf<List<Player>>(emptyList())

    var name by mutableStateOf("")
        private set

    var playersText by mutableStateOf("")
        private set

    var enableSave by mutableStateOf(false)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val paramTeamId: String? = savedStateHandle["team_id"]
        paramTeamId?.toLong()?.let { teamId ->
            viewModelScope.launch {
                repository.getTeam(teamId)?.let { team ->
                    // Store the team in the View Model
                    this@AddEditTeamViewModel.team = team
                    // Store the current name text in the View Model
                    setLocalTeamName(team.name)
                }
                playerRepo.getTeamPlayers(teamId).collect { ps ->
                    players = sortPlayers(ps)
                }
            }
        }
    }

    private fun setLocalTeamName(name: String) {
        // Store the name in the ViewModel
        this@AddEditTeamViewModel.name = name
        // Update the View Model's enableSave state based on if the name is different from the edit name.
        // NOTE: team will be null if this is a new team
        enableSave = name.isNotBlank() && name != team?.name
    }

    fun isEditMode(): Boolean {
        return team != null
    }

    fun onEvent(event: AddEditTeamEvent) {
        when (event) {
            is AddEditTeamEvent.OnNameChanged -> {
                setLocalTeamName(event.name)
            }

            is AddEditTeamEvent.OnPlayersChanged -> {
                playersText = event.players

                if (isEditMode() && playersText.length == 2) {
                    processJerseyNumber()
                }
            }

            is AddEditTeamEvent.OnPlayersChangedDone -> {
                if (isEditMode()) {
                    processJerseyNumber()
                }
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
                    playerRepo.deletePlayer(event.player)
                }
            }
        }
    }

    private fun processJerseyNumber() {
        val jerseyNumber = getJerseyFromString(playersText)
        if (jerseyNumber != null) {
            viewModelScope.launch {
                team!!.id?.let { addNewPlayer(it, jerseyNumber) }
            }
        } else {
            Log.w("JOEY", "Failed to get valid jersey Number")
        }
        // clear the text after this action
        playersText = ""
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
            if (name.isBlank()) {
                sendUiMessage("The name cannot be empty")
                return@launch
            }
            // Check for a duplicate Team name
            val count = repository.getTeamsWithName(name.trim())
            if (count > 0) {
                sendUiMessage("Duplicate Name")
                return@launch
            }
            // Insert or update the entity in repository.
            val id = repository.upsertTeam(
                Team(
                    name = name,
                    id = team?.id
                )
            )
            // If we are not already in edit mode then reload this Composable in edit more with
            // just created teamId
            if (!isEditMode()) {
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
        playerRepo.upsertPlayer(p)
        Log.d("JOEY", "Upsert Player $p")
    }

//    /**
//     * Gets a list of Jersey Numbers to be inserted in to the data repository.
//     * Blank characters will be removed, as well as any Jersey numbers exceeding 2 characters long
//     * and anything that does match the AlphaNumeric regex check. A leading zero is allowed.
//     * i.e. 00, or 01
//     *
//     * Any Jersey Number already in the data repository will be removed from this list
//     *
//     * @param data Comma or Space Separated String of Jersey Numbers
//     * @return List of valid String that can be added to the data repository
//     */
//    private fun getJerseyListFromString(data: String): List<String> {
//        if (data.isNotBlank()) {
//            val str = data.replace(' ', ',')
//            val jerseyNumbers = str.split(',').map { it.trim() }.distinct()
//                .filter { it.matches("^[a-zA-Z0-9]*$".toRegex()) && it.length <= 2 && it.isNotEmpty() }
//
//            // Remove duplicates from list
//            if (jerseyNumbers.isNotEmpty()) {
//                val existingJerseyNumber =
//                    players.stream().map { p -> p.jerseyNumber }.collect(Collectors.toList())
//                return jerseyNumbers.filterNot { existingJerseyNumber.contains(it) }
//            }
//        }
//        return emptyList()
//    }

    private fun getJerseyFromString(data: String): String? {
        if (data.isNotBlank() && data.trim().length <= 2 && data.trim()
                .matches("^[a-zA-Z0-9]*$".toRegex())
        ) {
            val jerseyNumber = data.trim()
            if (players.find { it.jerseyNumber == jerseyNumber } == null) {
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


