package com.ucfjoe.teamplayers.data.local

import com.ucfjoe.teamplayers.data.local.entity.toGame
import com.ucfjoe.teamplayers.data.local.entity.toGamePlayer
import com.ucfjoe.teamplayers.data.local.entity.toPlayer
import com.ucfjoe.teamplayers.data.local.entity.toTeam
import com.ucfjoe.teamplayers.data.relations.GameWithGamePlayersRelation
import com.ucfjoe.teamplayers.data.relations.TeamWithGamesRelation
import com.ucfjoe.teamplayers.data.relations.TeamWithPlayersRelation
import com.ucfjoe.teamplayers.domain.model.GameWithGamePlayers
import com.ucfjoe.teamplayers.domain.model.TeamWithGames
import com.ucfjoe.teamplayers.domain.model.TeamWithPlayers

fun TeamWithGamesRelation.toTeamWithGames(): TeamWithGames {
    return TeamWithGames(
        team = this.team.toTeam(),
        games = this.games.map { it.toGame() }
    )
}

fun TeamWithPlayersRelation.toTeamWithPlayers(): TeamWithPlayers {
    return TeamWithPlayers(
        team = this.team.toTeam(),
        players = this.players.map { it.toPlayer() }
    )
}

fun GameWithGamePlayersRelation.toGameWithGamePlayers(): GameWithGamePlayers {
    return GameWithGamePlayers(
        game = this.game.toGame(),
        gamePlayers = this.gamePlayers.map { it.toGamePlayer() }
    )
}