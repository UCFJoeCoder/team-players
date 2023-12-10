package com.ucfjoe.teamplayers.domain.use_case.games

import com.ucfjoe.teamplayers.domain.model.GameWithGamePlayers
import com.ucfjoe.teamplayers.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGameWithGamePlayersUseCase @Inject constructor (
    private val gameRepository: GameRepository
) {
    operator fun invoke(gameId: Long) : Flow<GameWithGamePlayers> {
        return gameRepository.getGameWithGamePlayers(gameId)
    }
}