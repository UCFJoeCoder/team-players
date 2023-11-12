package com.ucfjoe.teamplayers.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ucfjoe.teamplayers.database.entity.Game
import com.ucfjoe.teamplayers.database.entity.GamePlayer

data class GameWithGamePlayers(
    @Embedded val game: Game,
    @Relation(
        parentColumn = "id",
        entityColumn = "game_id",
        entity = GamePlayer::class
    )
    val gamePlayers: List<GamePlayer>? = null
)
