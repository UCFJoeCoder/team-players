package com.ucfjoe.teamplayers.repository

import com.ucfjoe.teamplayers.database.dao.PlayerDao
import com.ucfjoe.teamplayers.database.entity.Player
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val playerDao: PlayerDao
): PlayerRepository {
    override suspend fun upsertPlayer(player: Player): Long {
        return playerDao.upsertPlayer(player)
    }

    override suspend fun deletePlayer(player: Player) {
        playerDao.deletePlayer(player)
    }

    override suspend fun deletePlayerByJerseyNumber(teamId: Long, jerseyNumber: String) {
        playerDao.deletePlayerByJerseyNumber(teamId, jerseyNumber)
    }

    override fun getTeamPlayers(teamId: Long): Flow<List<Player>> {
        return playerDao.getTeamPlayers(teamId)
    }
}