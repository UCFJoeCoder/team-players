package com.ucfjoe.teamplayers.domain.use_case.game_players

import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.repository.GamePlayerRepository
import javax.inject.Inject

class IncrementSelectedGamePlayersUseCase @Inject constructor(
    private val gamePlayerRepository: GamePlayerRepository
) {
    suspend operator fun invoke(players: List<GamePlayer>): Resource<List<GamePlayer>> {
        if (players.count { it.isSelected } == 0) {
            return Resource.Error(message = "No players are selected!")
        }

        val editPlayers = players.map { gamePlayer ->
            if (gamePlayer.isSelected) {
                gamePlayer.copy(count = gamePlayer.count + 1, isSelected = false)
            } else {
                gamePlayer
            }
        }

        gamePlayerRepository.upsertGamePlayer(editPlayers)
        return Resource.Success(editPlayers)
    }
}