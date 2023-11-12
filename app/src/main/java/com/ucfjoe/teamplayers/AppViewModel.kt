package com.ucfjoe.teamplayers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucfjoe.teamplayers.database.DatabaseState
import com.ucfjoe.teamplayers.database.dao.PlayerDao
import com.ucfjoe.teamplayers.database.dao.TeamDao
import com.ucfjoe.teamplayers.database.entity.Player
import com.ucfjoe.teamplayers.database.entity.Team
import com.ucfjoe.teamplayers.events.TeamEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val teamDao: TeamDao,
    private val playerDao: PlayerDao
) : ViewModel() {
    private val _state = MutableStateFlow(DatabaseState())

    val state: StateFlow<DatabaseState> get() = _state

    fun onEvent(event: TeamEvent) {
        when (event) {
            is TeamEvent.deleteTeam -> {
                viewModelScope.launch {
                    teamDao.deleteTeam(event.team)
                }
            }

            TeamEvent.hideDialog -> TODO()
            TeamEvent.saveTeam -> {
                val name = state.value.teamName

                val team = Team(
                    name = name
                )

                viewModelScope.launch {
                    val id = teamDao.upsertTeam(team)
                    Log.d("AppViewModel", "$id")
                }
            }

            TeamEvent.savePlayers -> {
                val players = state.value.players

                for (p in players) {
                    viewModelScope.launch {
                        playerDao.upsertPlayer(p)
                    }
                }
            }

            is TeamEvent.setTeamName -> {
                _state.update {
                    it.copy(
                        teamName = event.teamName
                    )
                }
            }

            is TeamEvent.setPlayers -> {
                _state.update {
                    val players = getListOfPlayers(event.players)
                    it.copy(
                        players = players
                    )
                }
            }

            TeamEvent.showDialog -> TODO()
        }

    }

    fun getListOfPlayers(players: String): List<Player> {


//        val str = players.replace(' ', ',')
//        var strList = str.split(',').map { it.trim() }.distinct()
//            .filter { it.matches("^[a-zA-Z0-9]*$".toRegex()) && it.length <= 2 && it.isNotEmpty() }
//
//        val players: List<Player> = emptyList()
//        for (s in strList)
//        {
//            val player = Player(jerseyNumber = s, teamId = state.value.)
//        }

        return emptyList()
    }
}