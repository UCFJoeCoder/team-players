package com.ucfjoe.teamplayers.domain.repository

import com.ucfjoe.teamplayers.domain.model.GamePlayer
import kotlinx.coroutines.flow.Flow

interface GamePlayerRepository {

    suspend fun upsertGamePlayer(gamePlayer: GamePlayer): Long

    suspend fun upsertGamePlayer(gamePlayers: List<GamePlayer>)

    suspend fun deleteGamePlayer(gamePlayer: GamePlayer)

    suspend fun deleteGamePlayers(gameId: Long)

    fun getGamePlayers(gameId: Long): Flow<List<GamePlayer>>

    suspend fun insertGamePlayersFromTeamPlayers(gameId: Long, teamId: Long)

    suspend fun getDifferencesBetweenPlayersAndGamePlayers(
        gameId: Long,
        teamId: Long
    ): List<String>

    suspend fun getNumberOfPlayersWithJerseyNumber(
        playerId: Long,
        gameId: Long,
        jerseyNumber: String
    ): Int

}