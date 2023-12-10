package com.ucfjoe.teamplayers.domain.use_case.teams

import com.ucfjoe.teamplayers.domain.model.TeamWithGames
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTeamWithGamesUseCase @Inject constructor (
    private val teamRepository: TeamRepository
) {
    operator fun invoke(teamId: Long) : Flow<TeamWithGames> {
        return teamRepository.getTeamWithGames(teamId)
    }
}