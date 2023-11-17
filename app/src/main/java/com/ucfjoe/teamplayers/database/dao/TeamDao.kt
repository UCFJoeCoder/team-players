package com.ucfjoe.teamplayers.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ucfjoe.teamplayers.database.entity.Team
import com.ucfjoe.teamplayers.database.relations.TeamWithGames
import com.ucfjoe.teamplayers.database.relations.TeamWithPlayers
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Upsert
    suspend fun upsertTeam(team: Team): Long

    @Delete
    suspend fun deleteTeam(team: Team)

    @Query("SELECT * FROM teams WHERE id=:teamId")
    suspend fun getTeam(teamId: Long): Team?

    @Query("SELECT * FROM teams ORDER BY UPPER(name) ASC")
    fun getTeams(): Flow<List<Team>>

    @Query("SELECT COUNT(*) FROM teams where UPPER(name)=UPPER(:name)")
    suspend fun getTeamsWithName(name: String): Int

    @Transaction
    @Query("SELECT * FROM teams WHERE id=:teamId")
    fun getTeamWithGames(teamId: Long): Flow<List<TeamWithGames>>

    @Transaction
    @Query("SELECT * FROM teams WHERE id=:teamId")
    fun getTeamWithPlayers(teamId: Long): List<TeamWithPlayers>
}