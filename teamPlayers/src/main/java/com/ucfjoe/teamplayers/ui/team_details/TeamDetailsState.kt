package com.ucfjoe.teamplayers.ui.team_details

import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.Team

data class TeamDetailsState(
    val team: Team = Team(0, ""),
    val games: List<Game> = emptyList(),
    val isEditMode: Boolean = false
)
