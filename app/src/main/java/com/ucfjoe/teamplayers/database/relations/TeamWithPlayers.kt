package com.ucfjoe.teamplayers.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ucfjoe.teamplayers.database.entity.Player
import com.ucfjoe.teamplayers.database.entity.Team

data class TeamWithPlayers(
    @Embedded val team: Team,
    @Relation(
        parentColumn = "id",
        entityColumn = "team_id",
        entity = Player::class
    )
    val players: List<Player>? = null
)