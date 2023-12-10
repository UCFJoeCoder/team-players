package com.ucfjoe.teamplayers.domain.use_case.games

import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.repository.GameRepository
import java.time.LocalDateTime
import javax.inject.Inject

class UpsertGameUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(dateTime: LocalDateTime, teamId:Long, game:Game? = null): Resource<Game> {

        // If the game is not null then update its gameDateTime
        // If the game is null then create a new Game
        val upsertGame = game?.copy(gameDateTime = dateTime) ?: Game(teamId = teamId, gameDateTime = dateTime)

        val gameId = gameRepository.upsertGame(upsertGame)
        val returnGame = if (gameId==-1L) upsertGame else upsertGame.copy(id=gameId)

        return Resource.Success(returnGame)
    }
}