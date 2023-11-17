package com.ucfjoe.teamplayers.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "game_players",
    foreignKeys = [ForeignKey(
        entity = Game::class,
        parentColumns = ["id"],
        childColumns = ["game_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class GamePlayer(
    @ColumnInfo(name="game_id")
    val gameId: Long,

    @ColumnInfo(name="jersey_name")
    val jerseyName: String,

    @ColumnInfo(name="count")
    val count: Int,

    @ColumnInfo(name="is_absent")
    val isAbsent: Boolean,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0
)
