package com.ucfjoe.teamplayers.domain.model

import java.time.LocalDateTime

data class Game(
    val id: Long = 0,
    val teamId: Long,
    val gameDateTime: LocalDateTime,
    val isCompleted: Boolean = false
) : Comparable<Game> {
    override fun compareTo(other: Game): Int {
        return gameDateTime.compareTo(other.gameDateTime)
    }
}