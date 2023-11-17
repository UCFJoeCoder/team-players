//package com.ucfjoe.teamplayers.events
//
//import com.ucfjoe.teamplayers.database.entity.Player
//import com.ucfjoe.teamplayers.database.entity.Team
//
//sealed interface TeamEvent {
//    object saveTeam : TeamEvent
//    object savePlayers: TeamEvent
//    data class SetTeamName(val teamName: String) : TeamEvent
//    data class SetPlayers(val players: String) : TeamEvent
//    object showDialog : TeamEvent
//    object hideDialog : TeamEvent
//    data class DeleteTeam(val team: Team): TeamEvent
//}