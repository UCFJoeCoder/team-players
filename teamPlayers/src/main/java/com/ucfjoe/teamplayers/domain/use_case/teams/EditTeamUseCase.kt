package com.ucfjoe.teamplayers.domain.use_case.teams

import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
import javax.inject.Inject

class EditTeamUseCase @Inject constructor (
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(newName: String, oldName: String, teamId: Long): Resource<Unit> {
        if (newName.isBlank()) {
            return Resource.Error("Name cannot be empty.")
        }
        if (oldName == newName.trim()) {
            // The name didn't change. There is nothing to process
            return Resource.Success(Unit)
        }

        val count = teamRepository.getTeamsWithNameWithoutId(newName.trim(), teamId)
        if (count > 0) {
            return Resource.Error("Another team already has this name.")
        }

        val upsertTeam = Team(name = newName.trim(), id = teamId)

        teamRepository.upsertTeam(upsertTeam)

        return Resource.Success(Unit)
    }
}