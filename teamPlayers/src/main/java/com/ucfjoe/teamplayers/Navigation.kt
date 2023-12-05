package com.ucfjoe.teamplayers

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ucfjoe.teamplayers.ui.add_edit_game.AddEditGameScreen
import com.ucfjoe.teamplayers.ui.add_edit_team.AddEditTeamScreen
import com.ucfjoe.teamplayers.ui.game_details.GameDetailsScreen
import com.ucfjoe.teamplayers.ui.game_details.GameDetailsViewModel
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
            TeamsScreen(
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
        composable(
            route = Screen.AddEditTeamScreen.route + "?team_id={team_id}",
            arguments = listOf(
                navArgument("team_id") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            AddEditTeamScreen(
                onPopBackStack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it.route) }
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
                onNavigate = { navController.navigate(it.route) }
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
            val viewModel: GameDetailsViewModel = hiltViewModel()
            GameDetailsScreen(
                onPopBackStack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it.route) },
                viewModel.state.value,
                viewModel::onEvent
            )
        }
    }
}
