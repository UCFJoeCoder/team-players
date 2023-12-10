package com.ucfjoe.teamplayers.domain.use_case.game_players

import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.repository.GamePlayerRepository
import javax.inject.Inject

class ResetCountsToZeroUseCase @Inject constructor(
    private val gamePlayerRepository: GamePlayerRepository
) {
    suspend operator fun invoke(players: List<GamePlayer>) {
        val editPlayers = players.map { it.copy(count = 0) }
        gamePlayerRepository.upsertGamePlayer(editPlayers)
    }
}