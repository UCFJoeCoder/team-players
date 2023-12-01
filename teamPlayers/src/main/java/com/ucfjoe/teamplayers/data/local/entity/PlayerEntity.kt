package com.ucfjoe.teamplayers.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "players", foreignKeys = [ForeignKey(
        entity = TeamEntity::class,
        parentColumns = ["id"],
        childColumns = ["team_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PlayerEntity(
    @ColumnInfo(name = "jersey_number")
    val jerseyNumber: String,

    @ColumnInfo(name = "team_id", index=true)
    val teamId: Long,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", index = true)
    val id: Long = 0
)
