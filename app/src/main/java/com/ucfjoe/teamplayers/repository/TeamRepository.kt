package com.ucfjoe.teamplayers.repository

import com.ucfjoe.teamplayers.database.entity.Team
import com.ucfjoe.teamplayers.database.relations.TeamWithGames
import com.ucfjoe.teamplayers.database.relations.TeamWithPlayers
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    suspend fun upsertTeam(team:Team): Long

    suspend fun deleteTeam(team: Team)

    fun getTeams(): Flow<List<Team>>

    fun getTeamWithGames(teamId: Long): Flow<List<TeamWithGames>>

    fun getTeamWithPlayers(teamId: Long): Flow<List<TeamWithPlayers>>
}