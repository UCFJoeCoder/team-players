package com.ucfjoe.teamplayers.ui.add_edit_team

import com.ucfjoe.teamplayers.domain.model.Player

sealed class AddEditTeamEvent {

    data class OnNameChanged(val name: String) : AddEditTeamEvent()
    data class OnPlayersChanged(val players: String) : AddEditTeamEvent()
    object OnPlayersChangedDone : AddEditTeamEvent()
    object OnSaveTeamClick : AddEditTeamEvent()
    object OnDeleteTeamClick : AddEditTeamEvent()
    data class OnDeletePlayerClick(val player: Player) : AddEditTeamEvent()
}