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
fun CreateEditTeamScreen(
    navController: NavController,
    teamIdString: String?
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
    ) {
        Text(
            text=getTextString(teamIdString),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                navController.navigate(Screen.TeamDetailsScreen.route)
            }//,
            //modifier = Modifier.align(Alignment.Start)
        ) {
            Text(text = "To Team Details Screen")
        }
    }
}

fun getTextString(teamId: String?): String {
    return if (teamId==null) {
        "New Team"
    }
    else {
        "Edit Team $teamId"
    }
}

private fun isLong(str: String): Boolean {
    return try {
        str.toLong()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

fun String.toLong(): Long? {
    return try{
        toLong()
    } catch (e: NumberFormatException) {
        null
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    CreateEditTeamScreen(rememberNavController(),"1")
}
