package com.ucfjoe.teamplayers.repository

import com.ucfjoe.teamplayers.database.entity.Team
import com.ucfjoe.teamplayers.database.relations.TeamWithGames
import com.ucfjoe.teamplayers.database.relations.TeamWithPlayers
import kotlinx.coroutines.flow.Flow


interface TeamRepository {

    suspend fun upsertTeam(team:Team): Long

    suspend fun deleteTeam(team: Team)

    suspend fun getTeam(teamId: Long): Team?

    fun getTeams(): Flow<List<Team>>

    suspend fun getTeamsWithName(name: String): Int

    fun getTeamWithGames(teamId: Long): Flow<List<TeamWithGames>>

    suspend fun getTeamWithPlayers(teamId: Long): List<TeamWithPlayers>
}