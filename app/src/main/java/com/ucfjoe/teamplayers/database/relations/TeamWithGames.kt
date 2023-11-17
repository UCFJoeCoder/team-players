package com.ucfjoe.teamplayers.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ucfjoe.teamplayers.database.entity.Game
import com.ucfjoe.teamplayers.database.entity.Team

data class TeamWithGames (
    @Embedded val team: Team,
    @Relation(
        parentColumn = "id",
        entityColumn = "team_id",
        entity = Game::class
    )
    val games: List<Game>? = null
)