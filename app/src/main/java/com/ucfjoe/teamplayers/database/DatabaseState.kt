package com.ucfjoe.teamplayers.database

import com.ucfjoe.teamplayers.database.entity.Player
import com.ucfjoe.teamplayers.database.entity.Team

data class DatabaseState (
    val teams: List<Team> = emptyList(),
    val teamName: String = "",
    val players: List<Player> = emptyList(),
    val isTeamEditMode: Boolean = true
)