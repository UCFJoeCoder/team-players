package com.ucfjoe.teamplayers.domain.use_case.games

import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.repository.GameRepository
import javax.inject.Inject

class GetGameByIdUseCase @Inject constructor (
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(gameId: Long) : Game? {
        return gameRepository.getGame(gameId)
    }
}