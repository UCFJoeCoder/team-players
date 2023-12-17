package com.ucfjoe.teamplayers.domain.use_case.games

import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.repository.GameRepository
import javax.inject.Inject

class UpdateGameUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(game: Game): Resource<Unit> {

        if (game.id == 0L) {
            return Resource.Error("Update can only be called on an existing game")
        }

        gameRepository.upsertGame(game)

        return Resource.Success(Unit)
    }

}