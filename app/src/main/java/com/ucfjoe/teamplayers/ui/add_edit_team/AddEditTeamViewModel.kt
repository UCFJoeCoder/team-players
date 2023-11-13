package com.ucfjoe.teamplayers.ui.add_edit_team

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.database.entity.Team
import com.ucfjoe.teamplayers.repository.TeamRepository
import com.ucfjoe.teamplayers.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTeamViewModel @Inject constructor(
    private val repository: TeamRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var team by mutableStateOf<Team?>(null)
        private set

    var name by mutableStateOf("")
        private set

    var players by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val teamId = savedStateHandle.get<String>("team_id")
        teamId?.toLong()?.let {id ->
            viewModelScope.launch {
                repository.getTeam(id)?.let { team ->
                    name = team.name
                    this@AddEditTeamViewModel.team = team
                }
            }
        }
//        if (teamId != null) {
//            viewModelScope.launch {
//                repository.getTeam(teamId.toLong())?.let { team ->
//                    name = team.name
//                    this@AddEditTeamViewModel.team = team
//                }
//            }
//        }
    }

    fun onEvent(event: AddEditTeamEvent) {
        when (event) {
            is AddEditTeamEvent.OnNameChanged -> {
                name = event.name
            }

            is AddEditTeamEvent.OnPlayersChanged -> {
                players = event.players
            }

            is AddEditTeamEvent.OnSaveTeamClick -> {
                viewModelScope.launch {
                    // Make sure the data is valid before upsert
                    if (name.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "The name cannot be empty"
                            )
                        )
                        return@launch
                    }
                    // Insert or update the entity in repository.
                    repository.upsertTeam(
                        Team(
                            name = name,
                            id = team?.id
                        )
                    )
                    // After insert or update then move back to the previous screen.
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }

            is AddEditTeamEvent.OnDeleteTeamClick -> {
                viewModelScope.launch {
                    // TODO("Display dialog confirming delete team")
                    sendUiEvent(UiEvent.ShowSnackbar("onDelete", "onDeleteAction"))
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

//    private fun isLong(str: String): Boolean {
//        return try {
//            str.toLong()
//            true
//        } catch (e: NumberFormatException) {
//            false
//        }
//    }
//
//    fun String.toLong(): Long? {
//        return try {
//            toLong()
//        } catch (e: NumberFormatException) {
//            null
//        }
//    }
}


