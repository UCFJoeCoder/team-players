package com.ucfjoe.teamplayers.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ucfjoe.teamplayers.domain.model.Team

@Entity(tableName = "teams")
data class TeamEntity(
    @ColumnInfo(name = "name")
    val name: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", index = true)
    val id: Long = 0
)

fun TeamEntity.toTeam(): Team {
    return Team(
        id = this.id,
        name = this.name
    )
}

fun Team.toTeamEntity(): TeamEntity {
    return TeamEntity(
        id = this.id,
        name = this.name
    )
}
