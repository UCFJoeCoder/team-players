package com.ucfjoe.teamplayers.data.repository

import com.ucfjoe.teamplayers.data.local.dao.PlayerDao
import com.ucfjoe.teamplayers.data.local.toPlayer
import com.ucfjoe.teamplayers.data.local.toPlayerEntity
import com.ucfjoe.teamplayers.domain.model.Player
import com.ucfjoe.teamplayers.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val playerDao: PlayerDao
) : PlayerRepository {
    override suspend fun upsertPlayer(player: Player): Long {
        return playerDao.upsertPlayer(player.toPlayerEntity())
    }

    override suspend fun deletePlayer(player: Player) {
        playerDao.deletePlayer(player.toPlayerEntity())
    }

    override suspend fun deletePlayerByJerseyNumber(teamId: Long, jerseyNumber: String) {
        playerDao.deletePlayerByJerseyNumber(teamId, jerseyNumber)
    }

    override fun getTeamPlayers(teamId: Long): Flow<List<Player>> {
        return flow { playerDao.getTeamPlayers(teamId).map { it.toPlayer() } }
    }
}