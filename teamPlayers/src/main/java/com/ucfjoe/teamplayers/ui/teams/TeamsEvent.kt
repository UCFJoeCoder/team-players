package com.ucfjoe.teamplayers.ui.teams

import com.ucfjoe.teamplayers.domain.model.Team

sealed class TeamsEvent {

    data class OnTeamClick(val team: Team): TeamsEvent()

    data class OnDeleteClick(val team: Team): TeamsEvent()

    data object OnToggleEditMode: TeamsEvent()

    data object OnAddTeamClick: TeamsEvent()
    data object OnHideAddTeamDialog: TeamsEvent()
    data class OnProcessAddTeamRequest(val name: String): TeamsEvent()

}