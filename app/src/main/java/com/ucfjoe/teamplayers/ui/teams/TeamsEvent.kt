package com.ucfjoe.teamplayers.ui.teams

import com.ucfjoe.teamplayers.database.entity.Team

sealed class TeamsEvent {

    data class OnTeamClick(val team:Team): TeamsEvent()
    object OnAddTeamClick: TeamsEvent()

}