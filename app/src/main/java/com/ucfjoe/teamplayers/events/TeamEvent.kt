package com.ucfjoe.teamplayers.events

import com.ucfjoe.teamplayers.database.entity.Player
import com.ucfjoe.teamplayers.database.entity.Team

sealed interface TeamEvent {
    object saveTeam : TeamEvent
    object savePlayers: TeamEvent
    data class setTeamName(val teamName: String) : TeamEvent
    data class setPlayers(val players: String) : TeamEvent
    object showDialog : TeamEvent
    object hideDialog : TeamEvent
    data class deleteTeam(val team: Team): TeamEvent
}