package com.ucfjoe.teamplayers.repository

import com.ucfjoe.teamplayers.database.dao.TeamDao
import com.ucfjoe.teamplayers.database.entity.Team
import com.ucfjoe.teamplayers.database.relations.TeamWithGames
import com.ucfjoe.teamplayers.database.relations.TeamWithPlayers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val teamDao: TeamDao
): TeamRepository {

    override suspend fun upsertTeam(team: Team): Long {
        return teamDao.upsertTeam(team)
    }

    override suspend fun deleteTeam(team: Team) {
        teamDao.deleteTeam(team)
    }

    override suspend fun getTeam(teamId: Long): Team? {
        return teamDao.getTeam(teamId)
    }

    override fun getTeams(): Flow<List<Team>> {
        return teamDao.getTeams()
    }

    override suspend fun getTeamsWithName(name: String): Int {
        return teamDao.getTeamsWithName(name)
    }

    override fun getTeamWithGames(teamId: Long): Flow<List<TeamWithGames>> {
        return teamDao.getTeamWithGames(teamId)
    }

    override suspend fun getTeamWithPlayers(teamId: Long): List<TeamWithPlayers> {
        return teamDao.getTeamWithPlayers(teamId)
    }
}