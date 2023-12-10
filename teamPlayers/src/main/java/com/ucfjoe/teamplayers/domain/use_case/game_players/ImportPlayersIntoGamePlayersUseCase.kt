package com.ucfjoe.teamplayers.domain.use_case.game_players

import com.ucfjoe.teamplayers.domain.repository.GamePlayerRepository
import javax.inject.Inject

class ImportPlayersIntoGamePlayersUseCase @Inject constructor (
    private val gamePlayerRepository: GamePlayerRepository
) {
    suspend operator fun invoke(gameId: Long, teamId: Long) {
        gamePlayerRepository.insertGamePlayersFromTeamPlayers(gameId, teamId)
    }
}