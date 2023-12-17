package com.ucfjoe.teamplayers

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ucfjoe.teamplayers.ui.add_edit_game.AddEditGameScreen
import com.ucfjoe.teamplayers.ui.edit_team.EditTeamScreen
import com.ucfjoe.teamplayers.ui.game_details.GameDetailsScreen
import com.ucfjoe.teamplayers.ui.team_details.TeamDetailsScreen
import com.ucfjoe.teamplayers.ui.teams.TeamsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.TeamsScreen.route
    ) {
        composable(route = Screen.TeamsScreen.route) {
            TeamsScreen(onNavigate = { navController.navigate(it.route) })
        }
        composable(
            route = Screen.EditTeamScreen.route + "?team_id={team_id}",
            arguments = listOf(
                navArgument("team_id") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            EditTeamScreen(
                onPopBackStack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.TeamDetailsScreen.route + "?team_id={team_id}",
            arguments = listOf(
                navArgument("team_id") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            TeamDetailsScreen(
                onPopBackStack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it.route) }
            )
        }
        composable(
            route = Screen.AddEditGameScreen.route + "?team_id={team_id}&game_id={game_id}",
            arguments = listOf(
                navArgument("team_id") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("game_id") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            AddEditGameScreen(
                onPopBackStack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it.route) },
            )
        }
        composable(
            route = Screen.GameDetailsScreen.route + "?team_id={team_id}&game_id={game_id}",
            arguments = listOf(
                navArgument("team_id") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("game_id") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            GameDetailsScreen(onPopBackStack = { navController.popBackStack() })
        }
    }
}
