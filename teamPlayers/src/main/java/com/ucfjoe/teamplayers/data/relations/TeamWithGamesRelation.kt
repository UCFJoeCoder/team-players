package com.ucfjoe.teamplayers.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ucfjoe.teamplayers.data.local.entity.GameEntity
import com.ucfjoe.teamplayers.data.local.entity.TeamEntity

data class TeamWithGamesRelation (
    @Embedded val team: TeamEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "team_id",
        entity = GameEntity::class
    )
    val games: List<GameEntity> = emptyList()
)