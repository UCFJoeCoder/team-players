package com.ucfjoe.teamplayers.domain.use_case

import com.ucfjoe.teamplayers.domain.use_case.games.GetGameByIdUseCase
import com.ucfjoe.teamplayers.domain.use_case.games.UpsertGameUseCase
import com.ucfjoe.teamplayers.domain.use_case.teams.GetTeamByIdUseCase
import javax.inject.Inject

data class AddEditGameUseCases @Inject constructor (
    val getTeamByIdUseCase: GetTeamByIdUseCase,
    val getGameByIdUseCase: GetGameByIdUseCase,
    val upsertGameUseCase: UpsertGameUseCase
)
