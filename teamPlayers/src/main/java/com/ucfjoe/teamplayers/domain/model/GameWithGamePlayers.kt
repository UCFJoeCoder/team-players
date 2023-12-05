package com.ucfjoe.teamplayers.domain.model

data class GameWithGamePlayers(val game:Game, val gamePlayers: List<GamePlayer> = emptyList())
