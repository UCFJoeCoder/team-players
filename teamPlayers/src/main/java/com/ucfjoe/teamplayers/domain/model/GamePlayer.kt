package com.ucfjoe.teamplayers.domain.model

data class GamePlayer(val id: Long = 0, val gameId: Long, val jerseyNumber: String, val count: Int, val isAbsent: Boolean)
