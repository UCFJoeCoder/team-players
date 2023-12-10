package com.ucfjoe.teamplayers.domain.use_case.teams

import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTeamsUseCase @Inject constructor (
    private val teamRepository: TeamRepository
) {
    operator fun invoke() : Flow<List<Team>> {
        return teamRepository.getTeams()
    }
}