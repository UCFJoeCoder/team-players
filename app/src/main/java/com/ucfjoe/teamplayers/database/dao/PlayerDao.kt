package com.ucfjoe.teamplayers.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucfjoe.teamplayers.database.entity.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {


    @Upsert
    suspend fun upsertPlayer(player: Player)

    @Delete
    suspend fun deletePlayer(player: Player)

    @Query("SELECT * FROM players WHERE team_id = :teamId ORDER BY jersey_number ASC")
    fun getTeamPlayers(teamId:Long): Flow<List<Player>>
}