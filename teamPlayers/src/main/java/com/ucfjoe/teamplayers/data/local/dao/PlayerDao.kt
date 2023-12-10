package com.ucfjoe.teamplayers.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucfjoe.teamplayers.data.local.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Upsert
    suspend fun upsertPlayer(player: PlayerEntity): Long

    @Delete
    suspend fun deletePlayer(player: PlayerEntity)

    @Query("DELETE FROM players WHERE team_id=:teamId and jersey_number=:jerseyNumber")
    suspend fun deletePlayerByJerseyNumber(teamId:Long, jerseyNumber:String)

    @Query("SELECT * FROM players WHERE team_id = :teamId ORDER BY jersey_number ASC")
    fun getTeamPlayers(teamId:Long): Flow<List<PlayerEntity>>

    @Query("SELECT COUNT(*) FROM players WHERE team_id = :teamId AND jersey_number=:jerseyNumber")
    suspend fun getNumberOfPlayersWithJerseyNumber(teamId: Long, jerseyNumber: String) : Int
}