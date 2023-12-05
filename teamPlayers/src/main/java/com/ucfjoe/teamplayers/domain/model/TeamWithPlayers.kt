package com.ucfjoe.teamplayers.domain.model

data class TeamWithPlayers(val team:Team, val players: List<Player> = emptyList())
