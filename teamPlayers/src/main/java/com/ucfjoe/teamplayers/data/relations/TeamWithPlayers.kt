package com.ucfjoe.teamplayers.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ucfjoe.teamplayers.data.local.entity.PlayerEntity
import com.ucfjoe.teamplayers.data.local.entity.TeamEntity

data class TeamWithPlayers(
    @Embedded val team: TeamEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "team_id",
        entity = PlayerEntity::class
    )
    val players: List<PlayerEntity>? = null
)