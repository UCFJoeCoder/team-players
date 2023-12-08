package com.ucfjoe.teamplayers.ui.add_edit_team

import com.ucfjoe.teamplayers.domain.model.Player

sealed class AddEditTeamEvent {

    data class OnProcessSaveTeam(val name: String) : AddEditTeamEvent()
    data object OnShowEditTeamNameDialog : AddEditTeamEvent()
    data object OnHideEditTeamNameDialog : AddEditTeamEvent()

    data class OnPlayersChanged(val players: String) : AddEditTeamEvent()
    data object OnPlayersChangedDone : AddEditTeamEvent()
    data class OnDeletePlayerClick(val player: Player) : AddEditTeamEvent()
}