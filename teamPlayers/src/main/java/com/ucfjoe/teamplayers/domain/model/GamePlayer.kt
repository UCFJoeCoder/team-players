package com.ucfjoe.teamplayers.domain.model

import androidx.compose.ui.graphics.Color

data class GamePlayer(
    val id: Long = 0,
    val gameId: Long,
    val jerseyNumber: String,
    val count: Int = 0,
    val isAbsent: Boolean = false,
    val isSelected: Boolean = false
) {
    fun getStatusColor(): Color {
        return when {
            isSelected -> Color(0xFF87CEFA)
            count >= 8 -> Color.Green
            else -> Color.White
        }
    }
}
