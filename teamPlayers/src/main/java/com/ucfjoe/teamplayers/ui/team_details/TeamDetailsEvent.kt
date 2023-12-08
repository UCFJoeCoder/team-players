package com.ucfjoe.teamplayers.ui.team_details

import com.ucfjoe.teamplayers.domain.model.Game

sealed class TeamDetailsEvent {

    data class OnAddGameClick(val teamId: Long): TeamDetailsEvent()

    data object OnToggleEditMode: TeamDetailsEvent()

    data class OnGameClick(val game: Game): TeamDetailsEvent()

    data class OnDeleteGameClick(val game: Game): TeamDetailsEvent()

}
