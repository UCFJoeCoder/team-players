package com.ucfjoe.teamplayers.domain.use_case.teams

import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import javax.inject.Inject

class AddTeamUseCase @Inject constructor(
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(name:String): Resource<Team> {
        if (name.isBlank()){
            return Resource.Error(message = "Name cannot be empty.")
        }

        // Perform case insensitive search
        val count = teamRepository.getTeamsWithName(name.trim())
        if (count > 0) {
            return Resource.Error("Duplicate name detected.")
        }

        val insertTeam = Team(name = name.trim())
        val teamId = teamRepository.upsertTeam(insertTeam)
        return Resource.Success(insertTeam.copy(id = teamId))
    }
}