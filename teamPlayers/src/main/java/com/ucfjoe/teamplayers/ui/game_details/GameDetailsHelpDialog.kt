package com.ucfjoe.teamplayers.ui.game_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ucfjoe.teamplayers.R

@Preview
@Composable
fun GameDetailsHelpDialog(){
    GameDetailsHelpDialog({})
}

@Composable
fun GameDetailsHelpDialog(
    onDismissRequest: () -> Unit
) {
    val scrollState = rememberScrollState()
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.help_fill_24),
                    contentDescription = "Help"
                )
                Text("How To Use This Screen", style = MaterialTheme.typography.headlineSmall)
                Column(
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "Clicking on a player selects them and includes them in the current " +
                                "play. Select as many players as you want.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(6.dp)) {
                        Column(Modifier.fillMaxWidth(.4f)) {
                            Button(onClick = { }) {
                                Icon(Icons.Default.Add, "Increment Selection Example")
                                Text(text = "Add")
                            }
                        }
                        Column {
                            Text(
                                "Adds 1 to each of the selected players.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.secondaryContainer)
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(6.dp)) {
                        Column(Modifier.fillMaxWidth(.4f)) {
                            OutlinedButton(onClick = { }) {
                                Icon(
                                    painter = painterResource(R.drawable.repeat_fill_24),
                                    contentDescription = "Repeat Selection Example"
                                )
                                Text(text = "Repeat")
                            }
                        }
                        Column {
                            Text(
                                "Re-selected all the players who were in the previous play.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.secondaryContainer)
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(6.dp)) {
                        Column(Modifier.fillMaxWidth(.4f)) {
                            OutlinedButton(
                                onClick = { }
                            ) {
                                Icon(Icons.Default.Clear, "Clear Selection Example")
                                Text(text = "Clear")
                            }
                        }
                        Column(Modifier.fillMaxWidth()) {
                            Text(
                                "Clears all player selections.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.secondaryContainer)
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(6.dp)) {
                        Column(Modifier.fillMaxWidth(.4f)) {
                            Icon(
                                painter = painterResource(R.drawable.system_update_alt_fill_24),
                                contentDescription = "Import players from team"
                            )
                            Text("Import Players from Team")
                        }
                        Column {
                            Text(
                                "Removes all players and counts from the game and imports the " +
                                        "current team players.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.secondaryContainer)
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(6.dp)) {
                        Column(Modifier.fillMaxWidth(.4f)) {
                            Icon(
                                painter = painterResource(R.drawable.replay_fill_24),
                                contentDescription = "Reset all player's number of plays to zero"
                            )
                            Text("Reset Counts to Zero")
                        }
                        Column {
                            Text(
                                "Sets the number of plays performed in to 0 for all players.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.secondaryContainer)
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(6.dp)) {
                        Column(Modifier.fillMaxWidth(.4f)) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share game results")
                            Text("Share Game Results")
                        }
                        Column(Modifier.fillMaxWidth()) {
                            Text(
                                "Shares a file containing the games results.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Text(
                        "Game players can be imported from the team and can be different from " +
                                "the team's players. This is because players could have joined or " +
                                "left a team, but might have still participated in a game.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("Dismiss")
                    }
                }
            }
        }
    }
}