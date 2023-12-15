package com.ucfjoe.teamplayers.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ucfjoe.teamplayers.domain.model.GamePlayer

@Entity(
    tableName = "game_players",
    foreignKeys = [ForeignKey(
        entity = GameEntity::class,
        parentColumns = ["id"],
        childColumns = ["game_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class GamePlayerEntity(
    @ColumnInfo(name="game_id", index = true)
    val gameId: Long,

    @ColumnInfo(name="jersey_number")
    val jerseyNumber: String,

    @ColumnInfo(name="count")
    val count: Int,

    @ColumnInfo(name="is_absent")
    val isAbsent: Boolean,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0
)

fun GamePlayerEntity.toGamePlayer(): GamePlayer {
    return GamePlayer(
        id = this.id,
        gameId = this.gameId,
        jerseyNumber = this.jerseyNumber,
        count = this.count,
        isAbsent = this.isAbsent
    )
}

fun GamePlayer.toGamePlayerEntity(): GamePlayerEntity {
    return GamePlayerEntity(
        id = this.id,
        gameId = this.gameId,
        jerseyNumber = this.jerseyNumber,
        count = this.count,
        isAbsent = this.isAbsent
    )
}