package com.ucfjoe.teamplayers.domain.repository

import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.model.Team

fun interface WriteFileRepository {

    suspend fun writeFile(team: Team, game: Game, player: List<GamePlayer>): Resource<String>

}