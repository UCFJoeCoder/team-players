package com.ucfjoe.teamplayers.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucfjoe.teamplayers.data.local.entity.GamePlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GamePlayerDao {

    @Upsert
    suspend fun upsertGamePlayer(gamePlayer: GamePlayerEntity): Long

    @Upsert
    suspend fun upsertGamePlayer(gamePlayers: List<GamePlayerEntity>)

    @Delete
    suspend fun deleteGamePlayer(gamePlayer: GamePlayerEntity)

    @Query("DELETE FROM game_players WHERE game_id=:gameId")
    suspend fun deleteGamePlayers(gameId: Long)

    @Query("SELECT * FROM game_players WHERE game_id = :gameId ORDER BY jersey_number ASC")
    fun getGamePlayers(gameId: Long): Flow<List<GamePlayerEntity>>


    @Query(
        "INSERT INTO game_players (game_id, jersey_number, count, is_absent)" +
                "SELECT :gameId, jersey_number, 0, 0 FROM players WHERE team_id=:teamId"
    )
    suspend fun insertGamePlayersFromTeamPlayers(gameId: Long, teamId: Long)

    @Query(
        "SELECT COUNT(*) FROM game_players " +
                "WHERE game_id = :gameId AND id != :playerId AND UPPER(jersey_number) = UPPER(:jerseyNumber)"
    )
    suspend fun getNumberOfPlayersWithJerseyNumber(
        playerId: Long,
        gameId: Long,
        jerseyNumber: String
    ): Int
}