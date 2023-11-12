package com.ucfjoe.teamplayers.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.ucfjoe.teamplayers.database.entity.Team
import com.ucfjoe.teamplayers.repository.TeamRepository
import kotlinx.coroutines.runBlocking

class TeamViewModel(private val repository: TeamRepository) : ViewModel() {

//    val teams: LiveData<List<Team>> = repository.teams.asLiveData()
//
//    fun upsert(team: Team): Long {
//        var teamId: Long
//        runBlocking {
//            teamId = repository.upsertTeam(team)
//        }
//        return teamId
//    }
}

//class TeamViewModelFactory(private val repository: TeamRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(TeamViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return TeamViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}