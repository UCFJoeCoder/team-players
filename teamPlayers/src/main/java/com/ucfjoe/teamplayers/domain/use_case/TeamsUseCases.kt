package com.ucfjoe.teamplayers.domain.use_case

import com.ucfjoe.teamplayers.domain.use_case.teams.AddTeamUseCase
import com.ucfjoe.teamplayers.domain.use_case.teams.DeleteTeamUseCase
import com.ucfjoe.teamplayers.domain.use_case.teams.GetTeamsUseCase
import javax.inject.Inject

data class TeamsUseCases @Inject constructor(
    val getTeamsUseCase: GetTeamsUseCase,
    val deleteTeamUseCase: DeleteTeamUseCase,
    val addTeamUseCase: AddTeamUseCase
)
