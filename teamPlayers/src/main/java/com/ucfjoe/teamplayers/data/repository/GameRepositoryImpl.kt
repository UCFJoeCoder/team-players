package com.ucfjoe.teamplayers.data.repository

import com.ucfjoe.teamplayers.data.local.dao.GameDao
import com.ucfjoe.teamplayers.data.local.entity.toGame
import com.ucfjoe.teamplayers.data.local.entity.toGameEntity
import com.ucfjoe.teamplayers.data.local.toGameWithGamePlayers
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.GameWithGamePlayers
import com.ucfjoe.teamplayers.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val gameDao: GameDao
) : GameRepository {

    override suspend fun upsertGame(game: Game): Long {
        return gameDao.upsertGame(game.toGameEntity())
    }

    override suspend fun deleteGame(game: Game) {
        gameDao.deleteGame(game.toGameEntity())
    }

    override suspend fun getGame(gameId: Long): Game? {
        return gameDao.getGame(gameId)?.toGame()
    }

    override fun getGameWithGamePlayers(gameId: Long): Flow<GameWithGamePlayers> {
        return gameDao.getGameWithGamePlayers(gameId).map { it.toGameWithGamePlayers() }
    }
}