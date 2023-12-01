package com.ucfjoe.teamplayers.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucfjoe.teamplayers.data.local.entity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Upsert
    suspend fun upsertGame(game: GameEntity)

    @Delete
    suspend fun deleteGame(game: GameEntity)

    @Query("SELECT * FROM games WHERE team_id = :teamId ORDER BY date_time DESC")
    fun getTeamGames(teamId: Long): Flow<List<GameEntity>>

    @Query("SELECT MAX(id) FROM games")
    fun getMaxId(): Int
}