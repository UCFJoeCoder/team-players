package com.ucfjoe.teamplayers

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ucfjoe.teamplayers.ui.CreateEditGameScreen
import com.ucfjoe.teamplayers.ui.CreateEditTeamScreen
import com.ucfjoe.teamplayers.ui.GameDetailsScreen
import com.ucfjoe.teamplayers.ui.TeamDetailsScreen
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
            route = Screen.CreateEditTeamScreen.route + "?team_id={team_id}",
            arguments = listOf(
                navArgument("team_id") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            CreateEditTeamScreen(navController, it.arguments?.getString("team_id"))
        }
        composable(route = Screen.TeamDetailsScreen.route) {
            TeamDetailsScreen(navController = navController)
        }
        composable(route = Screen.CreateEditGameScreen.route) {
            CreateEditGameScreen(navController = navController)
        }
        composable(route = Screen.GameDetailsScreen.route) {
            GameDetailsScreen(navController = navController)
        }

//        composable(
//            route = Screen.EditTeamScreen.route + "/{name}",
//            arguments = listOf(
//                navArgument("name"){
//                    type = NavType.StringType
//                    defaultValue = "Joe"
//                    nullable = true
//                }
//            )
//        ) { entry ->
//            EditTeamScreen(name = entry.arguments?.getString("name"))
//        }
    }
}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainScreen(navController: NavController) {
//    var text by remember {
//        mutableStateOf("")
//    }
//    Column(
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 50.dp)
//    ) {
//        TextField(
//            value = text,
//            onValueChange = {
//                text = it
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(
//            onClick = {
//                navController.navigate(Screen.EditTeamScreen.route)
//            },
//            modifier = Modifier.align(Alignment.End)
//        ){
//            Text(text = "To Details Screen")
//        }
//
//    }
//}
//
//@Composable
//fun EditTeamScreen(name:String?) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier.fillMaxSize()
//    ) {
//      Text(text = "Hello, $name")
//    }
//}
