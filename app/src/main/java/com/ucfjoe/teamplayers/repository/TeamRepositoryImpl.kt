package com.ucfjoe.teamplayers.repository

import com.ucfjoe.teamplayers.database.dao.TeamDao
import com.ucfjoe.teamplayers.database.entity.Team
import com.ucfjoe.teamplayers.database.relations.TeamWithGames
import com.ucfjoe.teamplayers.database.relations.TeamWithPlayers
import kotlinx.coroutines.flow.Flow

class TeamRepositoryImpl(
    private val teamDao: TeamDao
): TeamRepository {

    override suspend fun upsertTeam(team: Team): Long {
        return teamDao.upsertTeam(team)
    }

    override suspend fun deleteTeam(team: Team) {
        teamDao.deleteTeam(team)
    }

    override fun getTeams(): Flow<List<Team>> {
        return teamDao.getTeams()
    }

    override fun getTeamWithGames(teamId: Long): Flow<List<TeamWithGames>> {
        return teamDao.getTeamWithGames(teamId)
    }

    override fun getTeamWithPlayers(teamId: Long): Flow<List<TeamWithPlayers>> {
        return teamDao.getTeamWithPlayers(teamId)
    }
}