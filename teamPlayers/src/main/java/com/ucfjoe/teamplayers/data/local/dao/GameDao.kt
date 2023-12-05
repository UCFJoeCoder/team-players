package com.ucfjoe.teamplayers.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ucfjoe.teamplayers.data.local.entity.GameEntity
import com.ucfjoe.teamplayers.data.relations.GameWithGamePlayersRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Upsert
    suspend fun upsertGame(game: GameEntity): Long

    @Delete
    suspend fun deleteGame(game: GameEntity)

    @Query("SELECT * FROM games WHERE id=:gameId")
    suspend fun getGame(gameId: Long): GameEntity?

    @Transaction
    @Query("SELECT * FROM games WHERE id=:gameId")
    fun getGameWithGamePlayers(gameId: Long): Flow<GameWithGamePlayersRelation>
}