package com.ucfjoe.teamplayers.domain.use_case.teams

import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import javax.inject.Inject

class GetTeamByIdUseCase @Inject constructor (
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(teamId: Long) : Team? {
        return teamRepository.getTeam(teamId)
    }
}