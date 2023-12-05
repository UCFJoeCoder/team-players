package com.ucfjoe.teamplayers.ui.game_details

import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.model.Team

data class GameDetailsState(
    val team: Team? = null,
    val game: Game? = null,
    val players: List<GamePlayer> = emptyList(),
    val askToImportCurrentPlayer: Boolean = false,
    val showPopupMenu: Boolean = false
)
