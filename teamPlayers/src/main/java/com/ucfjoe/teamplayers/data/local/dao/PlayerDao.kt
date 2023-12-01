package com.ucfjoe.teamplayers.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucfjoe.teamplayers.data.local.entity.PlayerEntity

@Dao
interface PlayerDao {

    @Upsert
    suspend fun upsertPlayer(player: PlayerEntity): Long

    @Delete
    suspend fun deletePlayer(player: PlayerEntity)

    @Query("DELETE FROM players WHERE team_id=:teamId and jersey_number=:jerseyNumber")
    suspend fun deletePlayerByJerseyNumber(teamId:Long, jerseyNumber:String)

    @Query("SELECT * FROM players WHERE team_id = :teamId ORDER BY jersey_number ASC")
    fun getTeamPlayers(teamId:Long): List<PlayerEntity>
}