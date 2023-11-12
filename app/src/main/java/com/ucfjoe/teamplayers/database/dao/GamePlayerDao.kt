package com.ucfjoe.teamplayers.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucfjoe.teamplayers.database.entity.GamePlayer
import kotlinx.coroutines.flow.Flow

@Dao
interface GamePlayerDao {

    @Upsert
    suspend fun upsertGamePlayer(gamePlayer: GamePlayer)

    @Delete
    suspend fun deleteGamePlayer(gamePlayer: GamePlayer)

    @Query("SELECT * FROM game_players WHERE game_id = :gameId ORDER BY jersey_name ASC")
    fun getGamePlayers(gameId: Long): Flow<List<GamePlayer>>


}