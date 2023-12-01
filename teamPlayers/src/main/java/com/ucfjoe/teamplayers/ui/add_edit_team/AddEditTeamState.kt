package com.ucfjoe.teamplayers.ui.add_edit_team

import com.ucfjoe.teamplayers.domain.model.Player
import com.ucfjoe.teamplayers.domain.model.Team

/**
 * Add Edit Team State
 *
 * @property team Stores the current team that is being displayed. Null if creating a new team
 * @property players List of players associated with the team above. Team cannot be null if players are available
 * @property nameText Name of the current team. can be edited for saving the name
 * @property playersText Players texted used for saving new players
 */
data class AddEditTeamState(
    val team: Team? = null,
    val players: List<Player> = emptyList(),
    val nameText: String = "",
    val playersText: String = ""
) {
    val editMode: Boolean = team != null
    val enableSave: Boolean = nameText.isNotBlank() && team?.name != nameText
}
