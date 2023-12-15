package com.ucfjoe.teamplayers.data.repository

import com.ucfjoe.teamplayers.data.local.dao.GamePlayerDao
import com.ucfjoe.teamplayers.data.local.entity.toGamePlayer
import com.ucfjoe.teamplayers.data.local.entity.toGamePlayerEntity
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.repository.GamePlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GamePlayerRepositoryImpl @Inject constructor(
    private val gamePlayerDao: GamePlayerDao
) : GamePlayerRepository {
    override suspend fun upsertGamePlayer(gamePlayer: GamePlayer): Long {
        return gamePlayerDao.upsertGamePlayer(gamePlayer.toGamePlayerEntity())
    }

    override suspend fun upsertGamePlayer(gamePlayers: List<GamePlayer>) {
        gamePlayerDao.upsertGamePlayer(gamePlayers.map { it.toGamePlayerEntity() })
    }

    override suspend fun deleteGamePlayer(gamePlayer: GamePlayer) {
        gamePlayerDao.deleteGamePlayer(gamePlayer.toGamePlayerEntity())
    }

    override suspend fun deleteGamePlayers(gameId: Long) {
        gamePlayerDao.deleteGamePlayers(gameId)
    }

    override fun getGamePlayers(gameId: Long): Flow<List<GamePlayer>> {
        return gamePlayerDao.getGamePlayers(gameId).map { list -> list.map { it.toGamePlayer() } }
    }

    override suspend fun insertGamePlayersFromTeamPlayers(gameId: Long, teamId: Long) {
        deleteGamePlayers(gameId)
        gamePlayerDao.insertGamePlayersFromTeamPlayers(gameId, teamId)
    }

    override suspend fun getDifferencesBetweenPlayersAndGamePlayers(
        gameId: Long,
        teamId: Long
    ): List<String> {
        return gamePlayerDao.getDifferencesBetweenPlayersAndGamePlayers(gameId, teamId)
    }

    override suspend fun getNumberOfPlayersWithJerseyNumber(
        playerId: Long,
        gameId: Long,
        jerseyNumber: String
    ): Int {
        return gamePlayerDao.getNumberOfPlayersWithJerseyNumber(playerId, gameId, jerseyNumber)
    }
}