package com.ucfjoe.teamplayers

sealed class Screen(val route:String)
{
    data object TeamsScreen: Screen("teams_screen")
    data object CreateEditTeamScreen: Screen("create_edit_team_screen")
    data object TeamDetailsScreen: Screen("team_details_screen")
    data object CreateEditGameScreen: Screen("create_edit_game_screen")
    data object GameDetailsScreen: Screen("game_details_screen")
}