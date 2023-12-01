package com.ucfjoe.teamplayers.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0
)
