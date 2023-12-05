package com.ucfjoe.teamplayers.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ucfjoe.teamplayers.data.local.entity.GameEntity
import com.ucfjoe.teamplayers.data.local.entity.GamePlayerEntity

data class GameWithGamePlayersRelation(
    @Embedded val game: GameEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "game_id",
        entity = GamePlayerEntity::class
    )
    val gamePlayers: List<GamePlayerEntity> = emptyList()
)
