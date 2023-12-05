package com.ucfjoe.teamplayers.domain.repository

import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.model.TeamWithGames
import com.ucfjoe.teamplayers.domain.model.TeamWithPlayers
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    suspend fun upsertTeam(team: Team): Long

    suspend fun deleteTeam(team: Team)

    suspend fun getTeam(teamId: Long): Team?

    fun getTeams(): Flow<List<Team>>

    suspend fun getTeamsWithName(name: String): Int

    suspend fun getTeamsWithNameCaseSensitive(name: String): Int

    fun getTeamWithGames(teamId: Long): Flow<TeamWithGames>

    fun getTeamWithPlayers(teamId: Long): Flow<TeamWithPlayers>
}