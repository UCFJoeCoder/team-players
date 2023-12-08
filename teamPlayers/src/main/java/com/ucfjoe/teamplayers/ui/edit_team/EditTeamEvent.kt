package com.ucfjoe.teamplayers.ui.edit_team

import com.ucfjoe.teamplayers.domain.model.Player

sealed class EditTeamEvent {

    data class OnProcessSaveTeam(val name: String) : EditTeamEvent()
    data object OnShowEditTeamNameDialog : EditTeamEvent()
    data object OnHideEditTeamNameDialog : EditTeamEvent()

    data class OnPlayersChanged(val players: String) : EditTeamEvent()
    data object OnPlayersChangedDone : EditTeamEvent()
    data class OnDeletePlayerClick(val player: Player) : EditTeamEvent()
}