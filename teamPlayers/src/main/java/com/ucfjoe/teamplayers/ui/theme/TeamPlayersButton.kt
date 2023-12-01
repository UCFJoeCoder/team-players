package com.ucfjoe.teamplayers.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TeamPlayersButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier.padding(5.dp),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(50),
        colors = teamPlayerButtonColors()
    )
    {
        Text(text = text)
    }
}

@Composable
fun teamPlayerButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = Color(0xFF27A7D6),
        contentColor = Color.White,
        disabledContainerColor = Color(0xFF81CCF7),
        disabledContentColor = Color.Gray)
}

@Preview
@Composable
fun PreviewTeamPlayersButton() {
    TeamPlayersButton(
        text = "Test",
        onClick = {}
    )
}
