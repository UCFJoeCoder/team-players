package com.ucfjoe.teamplayers.data.local

import com.ucfjoe.teamplayers.data.local.entity.GameEntity
import com.ucfjoe.teamplayers.data.local.entity.GamePlayerEntity
import com.ucfjoe.teamplayers.data.local.entity.PlayerEntity
import com.ucfjoe.teamplayers.data.local.entity.TeamEntity
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.model.Player
import com.ucfjoe.teamplayers.domain.model.Team

fun GameEntity.toGame(): Game {
    return Game(
        id = this.id,
        teamId = this.teamId,
        gameDateTime = this.dateTime
    )
}

fun Game.toGameEntity(): GameEntity {
    return GameEntity(
        id = this.id,
        teamId = this.teamId,
        dateTime = this.gameDateTime
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