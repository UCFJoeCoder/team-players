package com.ucfjoe.teamplayers.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ucfjoe.teamplayers.data.local.entity.TeamEntity
import com.ucfjoe.teamplayers.data.relations.TeamWithGames
import com.ucfjoe.teamplayers.data.relations.TeamWithPlayers

@Dao
interface TeamDao {

    @Upsert
    suspend fun upsertTeam(team: TeamEntity): Long

    @Delete
    suspend fun deleteTeam(team: TeamEntity)

    @Query("SELECT * FROM teams WHERE id=:teamId")
    suspend fun getTeam(teamId: Long): TeamEntity?

    @Query("SELECT * FROM teams ORDER BY UPPER(name) ASC")
    fun getTeams(): List<TeamEntity>

    @Query("SELECT COUNT(*) FROM teams where UPPER(name)=UPPER(:name)")
    suspend fun getTeamsWithName(name: String): Int

    @Transaction
    @Query("SELECT * FROM teams WHERE id=:teamId")
    fun getTeamWithGames(teamId: Long): List<TeamWithGames>

    @Transaction
    @Query("SELECT * FROM teams WHERE id=:teamId")
    fun getTeamWithPlayers(teamId: Long): List<TeamWithPlayers>
}