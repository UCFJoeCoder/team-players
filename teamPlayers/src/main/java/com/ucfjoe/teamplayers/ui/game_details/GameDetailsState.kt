package com.ucfjoe.teamplayers.ui.game_details

import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.model.Team

data class GameDetailsState(
    val team: Team? = null,
    val game: Game? = null,
    val players: List<GamePlayer> = emptyList(),
    val showPopupMenu: Boolean = false,
    val showImportCurrentPlayerDialog: Boolean = false,
    val showHelpDialog: Boolean = false,
    val showShareGameDetailsDialog: Boolean = false,
    val showRequestClearCountDialog: Boolean = false,
    val showEditPlayerDialog: Boolean = false,
    val showCompleteGameDialog: Boolean = false,
    val editPlayer: GamePlayer? = null,
    val editErrorMessage: String? = null,
    val emailErrorMessage: String? = null
)
