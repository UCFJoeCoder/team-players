package com.ucfjoe.teamplayers.domain.model

data class TeamWithGames(val team: Team, val games: List<Game> = emptyList())
