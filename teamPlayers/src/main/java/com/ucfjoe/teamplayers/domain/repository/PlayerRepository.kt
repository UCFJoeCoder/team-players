package com.ucfjoe.teamplayers.domain.repository

import com.ucfjoe.teamplayers.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    suspend fun upsertPlayer(player: Player): Long

    suspend fun deletePlayer(player: Player)

    suspend fun deletePlayerByJerseyNumber(teamId: Long, jerseyNumber: String)

    fun getTeamPlayers(teamId: Long): Flow<List<Player>>

    suspend fun getNumberOfPlayersWithJerseyNumber(teamId: Long, jerseyNumber: String) : Int
}