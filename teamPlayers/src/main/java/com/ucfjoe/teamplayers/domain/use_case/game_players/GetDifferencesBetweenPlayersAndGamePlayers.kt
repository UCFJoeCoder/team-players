package com.ucfjoe.teamplayers.domain.use_case.game_players

import com.ucfjoe.teamplayers.domain.repository.GamePlayerRepository
import javax.inject.Inject

class GetDifferencesBetweenPlayersAndGamePlayers @Inject constructor(
    private val gamePlayerRepository: GamePlayerRepository
) {
    suspend operator fun invoke(gameId: Long, teamId: Long): List<String> {
        return gamePlayerRepository.getDifferencesBetweenPlayersAndGamePlayers(gameId, teamId)
    }
}