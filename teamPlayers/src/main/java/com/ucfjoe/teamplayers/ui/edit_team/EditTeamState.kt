package com.ucfjoe.teamplayers.ui.edit_team

import com.ucfjoe.teamplayers.domain.model.Player
import com.ucfjoe.teamplayers.domain.model.Team

/**
 * Add Edit Team State
 *
 * @property team Stores the current team that is being displayed. Null if creating a new team
 * @property players List of players associated with the team above. Team cannot be null if players are available
 * @property playersText Players texted used for saving new players
 * @property saveTeamError null if no error; otherwise contains a validation error message
 * @property showEditTeamNameDialog true to display the dialog; false otherwise
 */
data class EditTeamState(
    val team: Team? = null,
    val players: List<Player> = emptyList(),
    val playersText: String = "",
    val savePlayerError: String? = null,
    val saveTeamError: String? = null,
    val showEditTeamNameDialog: Boolean = false
)
