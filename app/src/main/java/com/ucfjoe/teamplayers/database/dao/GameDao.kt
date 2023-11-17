package com.ucfjoe.teamplayers.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucfjoe.teamplayers.database.entity.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Upsert
    suspend fun upsertGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)

    @Query("SELECT * FROM games WHERE team_id = :teamId ORDER BY date_time DESC")
    fun getTeamGames(teamId: Long): Flow<List<Game>>

    @Query("SELECT MAX(id) FROM games")
    fun getMaxId(): Int
}