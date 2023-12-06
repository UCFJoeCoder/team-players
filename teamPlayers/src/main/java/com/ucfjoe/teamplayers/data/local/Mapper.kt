package com.ucfjoe.teamplayers.data.local

import com.ucfjoe.teamplayers.data.local.entity.GameEntity
import com.ucfjoe.teamplayers.data.local.entity.GamePlayerEntity
import com.ucfjoe.teamplayers.data.local.entity.PlayerEntity
import com.ucfjoe.teamplayers.data.local.entity.TeamEntity
import com.ucfjoe.teamplayers.data.relations.GameWithGamePlayersRelation
import com.ucfjoe.teamplayers.data.relations.TeamWithGamesRelation
import com.ucfjoe.teamplayers.data.relations.TeamWithPlayersRelation
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.model.GameWithGamePlayers
import com.ucfjoe.teamplayers.domain.model.Player
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.model.TeamWithGames
import com.ucfjoe.teamplayers.domain.model.TeamWithPlayers

fun GameEntity.toGame(): Game {
    return Game(
        id = this.id,
        teamId = this.teamId,
        gameDateTime = this.dateTime,
        isCompleted = this.isCompleted
    )
}

fun Game.toGameEntity(): GameEntity {
    return GameEntity(
        id = this.id,
        teamId = this.teamId,
        dateTime = this.gameDateTime,
        isCompleted = this.isCompleted
    )
}

fun GamePlayerEntity.toGamePlayer(): GamePlayer {
    return GamePlayer(
        id = this.id,
        gameId = this.gameId,
        jerseyNumber = this.jerseyNumber,
        count = this.count,
        isAbsent = this.isAbsent
    )
}

fun GamePlayer.toGamePlayerEntity(): GamePlayerEntity {
    return GamePlayerEntity(
        id = this.id,
        gameId = this.gameId,
        jerseyNumber = this.jerseyNumber,
        count = this.count,
        isAbsent = this.isAbsent
    )
}

fun PlayerEntity.toPlayer(): Player {
    return Player(
        id = this.id,
        teamId = this.teamId,
        jerseyNumber = this.jerseyNumber
    )
}

fun Player.toPlayerEntity(): PlayerEntity {
    return PlayerEntity(
        id = this.id,
        teamId = this.teamId,
        jerseyNumber = this.jerseyNumber
    )
}

fun TeamEntity.toTeam(): Team {
    return Team(
        id = this.id,
        name = this.name
    )
}

fun Team.toTeamEntity(): TeamEntity {
    return TeamEntity(
        id = this.id,
        name = this.name
    )
}

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