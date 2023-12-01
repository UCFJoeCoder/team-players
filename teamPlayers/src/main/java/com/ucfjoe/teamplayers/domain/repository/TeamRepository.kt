package com.ucfjoe.teamplayers.domain.repository

import com.ucfjoe.teamplayers.data.relations.TeamWithGames
import com.ucfjoe.teamplayers.data.relations.TeamWithPlayers
import com.ucfjoe.teamplayers.domain.model.Team
import kotlinx.coroutines.flow.Flow


interface TeamRepository {

    suspend fun upsertTeam(team: Team): Long

    suspend fun deleteTeam(team: Team)

    suspend fun getTeam(teamId: Long): Team?

    fun getTeams(): Flow<List<Team>>

    suspend fun getTeamsWithName(name: String): Int

    fun getTeamWithGames(teamId: Long): Flow<List<TeamWithGames>>

    suspend fun getTeamWithPlayers(teamId: Long): List<TeamWithPlayers>
}