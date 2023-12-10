package com.ucfjoe.teamplayers.domain.use_case.players

import com.ucfjoe.teamplayers.domain.model.Player
import com.ucfjoe.teamplayers.domain.repository.PlayerRepository
import javax.inject.Inject

class DeletePlayerUseCase @Inject constructor (
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(player: Player) {
        playerRepository.deletePlayer(player)
    }
}