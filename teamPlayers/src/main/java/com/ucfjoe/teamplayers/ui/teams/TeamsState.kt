package com.ucfjoe.teamplayers.ui.teams

import com.ucfjoe.teamplayers.domain.model.Team

data class TeamsState(
    val teams: List<Team> = emptyList(),
    val isEditMode: Boolean = false,
    val addTeamErrorMessage: String? = null,
    val showAddTeamDialog: Boolean = false
)
