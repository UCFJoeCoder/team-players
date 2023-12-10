package com.ucfjoe.teamplayers.domain.use_case.game_players

import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.repository.GamePlayerRepository
import javax.inject.Inject

class EditGamePlayerUseCase @Inject constructor (
    private val gamePlayerRepository: GamePlayerRepository
) {
    suspend operator fun invoke(
        playerId: Long, gameId: Long, jerseyNumber: String, count: Int, isAbsent: Boolean
    ): Resource<Unit> {

        if (jerseyNumber.isBlank()) {
            return Resource.Error(message = "Jersey Number must be provided")
        }
        if (jerseyNumber.length > 2) {
            return Resource.Error(message = "Jersey Number must be at most 2 characters")
        }
        if (!jerseyNumber.matches("^[a-zA-Z0-9]*$".toRegex())) {
            return Resource.Error(message = "Jersey Number can only contain numbers and letters")

        }
        if (count < 0 || count > 100) {
            return Resource.Error(message = "Number of Plays must be between 0 and 100")
        }

        val duplicateJerseyNumbers = gamePlayerRepository.getNumberOfPlayersWithJerseyNumber(
            playerId,
            gameId,
            jerseyNumber
        )
        if (duplicateJerseyNumbers > 0) {
            return Resource.Error(message = "Duplicate jersey number")
        }

        val editPlayer = GamePlayer(playerId, gameId, jerseyNumber, count, isAbsent)
        gamePlayerRepository.upsertGamePlayer(editPlayer)

        return Resource.Success(Unit)
    }
}