package com.ucfjoe.teamplayers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ucfjoe.teamplayers.Screen

@Composable
fun TeamDetailsScreen(
    navController: NavController
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
    ) {
        Text(
            text="Team Details Screen",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                navController.navigate(Screen.CreateEditGameScreen.route)
            }
        ) {
            Text(text = "To Create Edit Game Screen")
        }

        Button(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text(text = "Go Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTeamDetailsScreen() {
    TeamDetailsScreen(rememberNavController())
}