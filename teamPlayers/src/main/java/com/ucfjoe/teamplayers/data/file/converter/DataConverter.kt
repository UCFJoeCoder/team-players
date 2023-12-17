package com.ucfjoe.teamplayers.data.file.converter

import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.model.Team

fun interface DataConverter {

    fun convertData(team: Team, game: Game, players: List<GamePlayer>): Resource<GamePlayerCsvInfo>

}