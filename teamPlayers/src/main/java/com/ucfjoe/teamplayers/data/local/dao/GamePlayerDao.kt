package com.ucfjoe.teamplayers.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucfjoe.teamplayers.data.local.entity.GamePlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GamePlayerDao {

    @Upsert
    suspend fun upsertGamePlayer(gamePlayer: GamePlayerEntity)

    @Delete
    suspend fun deleteGamePlayer(gamePlayer: GamePlayerEntity)

    @Query("SELECT * FROM game_players WHERE game_id = :gameId ORDER BY jersey_number ASC")
    fun getGamePlayers(gameId: Long): Flow<List<GamePlayerEntity>>


}