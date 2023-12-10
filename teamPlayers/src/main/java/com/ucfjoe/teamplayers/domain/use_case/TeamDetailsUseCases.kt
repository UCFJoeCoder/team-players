package com.ucfjoe.teamplayers.domain.use_case

import com.ucfjoe.teamplayers.domain.use_case.games.DeleteGameUseCase
import com.ucfjoe.teamplayers.domain.use_case.teams.GetTeamWithGamesUseCase
import javax.inject.Inject

data class TeamDetailsUseCases @Inject constructor(
    val getTeamWithGamesUseCase: GetTeamWithGamesUseCase,
    val deleteGameUseCase: DeleteGameUseCase
)
