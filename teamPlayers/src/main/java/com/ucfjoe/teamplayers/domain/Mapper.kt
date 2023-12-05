package com.ucfjoe.teamplayers.domain

import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.model.Player

fun Player.toNewGamePlayer(gameId: Long) : GamePlayer {
    return GamePlayer(
        id = 0,
        gameId = gameId,
        jerseyNumber = this.jerseyNumber,
        count = 0,
        isAbsent = false
    )
}