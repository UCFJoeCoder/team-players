package com.ucfjoe.teamplayers.ui.add_edit_game

import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.Team
import java.time.LocalDate
import java.time.LocalTime

data class AddEditGameState(
    val teamId: Long = 0,
    val team: Team? = null,
    val game: Game? = null,
    val saveError: String? = null,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = getDefaultTime(),
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false
) {
    val isEditMode: Boolean = game != null

    companion object {
        private fun getDefaultTime(): LocalTime {
            var hour = LocalTime.now().hour
            var minute = LocalTime.now().minute

            if (minute != 0 && minute != 30) {
                if (minute < 30) {
                    minute = 30
                } else {
                    minute = 0
                    hour++
                    if (hour > 23) hour = 0
                }
            }

            return LocalTime.of(hour, minute)
        }
    }
}

