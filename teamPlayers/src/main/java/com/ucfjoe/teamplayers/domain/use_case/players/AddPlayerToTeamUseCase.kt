package com.ucfjoe.teamplayers.domain.use_case.players

import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.model.Player
import com.ucfjoe.teamplayers.domain.repository.PlayerRepository
import javax.inject.Inject

class AddPlayerToTeamUseCase @Inject constructor (
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(jerseyNumber: String, teamId: Long): Resource<Player> {

        if (jerseyNumber.isBlank()) {
            return Resource.Error(message = "Jersey Number cannot be blank.")
        }

        if (jerseyNumber.length > 2) {
            return Resource.Error(message = "Jersey Number cannot exceed 2 characters.")
        }

        if (!jerseyNumber.matches("^[a-zA-Z0-9]*$".toRegex())) {
            return Resource.Error(message = "Jersey Numbers can only contain number or letters.")
        }

        val count = playerRepository.getNumberOfPlayersWithJerseyNumber(teamId, jerseyNumber)
        if (count > 0) {
            return Resource.Error(message = "There is already a player with that jersey number")
        }

        val player = Player(teamId = teamId, jerseyNumber = jerseyNumber)
        val playerId = playerRepository.upsertPlayer(player)
        return Resource.Success(player.copy(id = playerId))
    }
}