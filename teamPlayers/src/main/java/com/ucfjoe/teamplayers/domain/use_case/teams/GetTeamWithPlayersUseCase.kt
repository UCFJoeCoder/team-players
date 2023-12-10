package com.ucfjoe.teamplayers.domain.use_case.teams

import com.ucfjoe.teamplayers.domain.model.TeamWithPlayers
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTeamWithPlayersUseCase @Inject constructor (
    private val teamRepository: TeamRepository
) {
    operator fun invoke(teamId: Long) : Flow<TeamWithPlayers>  {
        return teamRepository.getTeamWithPlayers(teamId)
    }
}