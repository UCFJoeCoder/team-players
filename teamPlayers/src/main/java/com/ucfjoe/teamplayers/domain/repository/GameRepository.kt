package com.ucfjoe.teamplayers.domain.repository

import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.GameWithGamePlayers
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun upsertGame(game:Game): Long

    suspend fun deleteGame(game:Game)

    suspend fun getGame(gameId: Long): Game?

    fun getGameWithGamePlayers(gameId: Long): Flow<GameWithGamePlayers>

}