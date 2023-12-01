package com.ucfjoe.teamplayers.data.repository

import com.ucfjoe.teamplayers.data.local.dao.TeamDao
import com.ucfjoe.teamplayers.data.local.toTeam
import com.ucfjoe.teamplayers.data.local.toTeamEntity
import com.ucfjoe.teamplayers.data.relations.TeamWithGames
import com.ucfjoe.teamplayers.data.relations.TeamWithPlayers
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val teamDao: TeamDao
) : TeamRepository {

    override suspend fun upsertTeam(team: Team): Long {
        return teamDao.upsertTeam(team.toTeamEntity())
    }

    override suspend fun deleteTeam(team: Team) {
        teamDao.deleteTeam(team.toTeamEntity())
    }

    override suspend fun getTeam(teamId: Long): Team? {
        return teamDao.getTeam(teamId)?.toTeam()
    }

    override fun getTeams(): Flow<List<Team>> {
        return flow { teamDao.getTeams().map { it.toTeam() } }
    }

    override suspend fun getTeamsWithName(name: String): Int {
        return teamDao.getTeamsWithName(name)
    }

    override fun getTeamWithGames(teamId: Long): Flow<List<TeamWithGames>> {
        return flow { teamDao.getTeamWithGames(teamId) }
    }

    override suspend fun getTeamWithPlayers(teamId: Long): List<TeamWithPlayers> {
        return teamDao.getTeamWithPlayers(teamId)
    }
}