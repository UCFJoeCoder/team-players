package com.ucfjoe.teamplayers.domain.use_case.teams

import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import javax.inject.Inject

class DeleteTeamUseCase @Inject constructor(
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(team: Team) {
        teamRepository.deleteTeam(team)
    }
}