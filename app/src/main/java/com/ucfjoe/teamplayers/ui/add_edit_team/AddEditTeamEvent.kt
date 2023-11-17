package com.ucfjoe.teamplayers.ui.add_edit_team

import com.ucfjoe.teamplayers.database.entity.Player
import com.ucfjoe.teamplayers.database.entity.Team

sealed class AddEditTeamEvent {

    data class OnNameChanged(val name: String) : AddEditTeamEvent()
    data class OnPlayersChanged(val players: String) : AddEditTeamEvent()
    object OnPlayersChangedDone : AddEditTeamEvent()
    object OnSaveTeamClick : AddEditTeamEvent()
    object OnDeleteTeamClick : AddEditTeamEvent()
    data class OnDeletePlayerClick(val player: Player) : AddEditTeamEvent()
}