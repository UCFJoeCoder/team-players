package com.ucfjoe.teamplayers.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ucfjoe.teamplayers.domain.model.Game
import java.time.LocalDateTime

@Entity(
    tableName = "games",
    foreignKeys = [ForeignKey(
        entity = TeamEntity::class,
        parentColumns = ["id"],
        childColumns = ["team_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class GameEntity(
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime,

    @ColumnInfo(name = "team_id", index=true)
    val teamId: Long,

    @ColumnInfo(name = "is_completed", defaultValue = "0")
    val isCompleted: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0
)

fun GameEntity.toGame(): Game {
    return Game(
        id = this.id,
        teamId = this.teamId,
        gameDateTime = this.dateTime,
        isCompleted = this.isCompleted
    )
}

fun Game.toGameEntity(): GameEntity {
    return GameEntity(
        id = this.id,
        teamId = this.teamId,
        dateTime = this.gameDateTime,
        isCompleted = this.isCompleted
    )
}
