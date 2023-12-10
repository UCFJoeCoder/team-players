package com.ucfjoe.teamplayers.domain.use_case

import com.ucfjoe.teamplayers.domain.use_case.players.AddPlayerToTeamUseCase
import com.ucfjoe.teamplayers.domain.use_case.players.DeletePlayerUseCase
import com.ucfjoe.teamplayers.domain.use_case.teams.EditTeamUseCase
import com.ucfjoe.teamplayers.domain.use_case.teams.GetTeamWithPlayersUseCase
import javax.inject.Inject

data class EditTeamUseCases @Inject constructor(
    val addPlayerToTeamUseCase: AddPlayerToTeamUseCase,
    val deletePlayerUseCase: DeletePlayerUseCase,
    val getTeamWithPlayersUseCase: GetTeamWithPlayersUseCase,
    val editTeamUseCase: EditTeamUseCase,
)