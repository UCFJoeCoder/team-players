package com.ucfjoe.teamplayers.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ucfjoe.teamplayers.data.local.entity.TeamEntity
import com.ucfjoe.teamplayers.data.relations.TeamWithGamesRelation
import com.ucfjoe.teamplayers.data.relations.TeamWithPlayersRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Upsert
    suspend fun upsertTeam(team: TeamEntity): Long

    @Delete
    suspend fun deleteTeam(team: TeamEntity)

    @Query("SELECT * FROM teams WHERE id=:teamId")
    suspend fun getTeam(teamId: Long): TeamEntity?

    @Query("SELECT * FROM teams ORDER BY UPPER(name) ASC")
    fun getTeams(): Flow<List<TeamEntity>>

    @Query("SELECT COUNT(*) FROM teams where UPPER(name)=UPPER(:name)")
    suspend fun getTeamsWithName(name: String): Int

    @Query("SELECT COUNT(*) FROM teams where name=:name")
    suspend fun getTeamsWithNameCaseSensitive(name: String): Int

    @Transaction
    @Query("SELECT * FROM teams WHERE id=:teamId")
    fun getTeamWithGames(teamId: Long): Flow<TeamWithGamesRelation>

    @Transaction
    @Query("SELECT * FROM teams WHERE id=:teamId")
    fun getTeamWithPlayers(teamId: Long): Flow<TeamWithPlayersRelation>
}