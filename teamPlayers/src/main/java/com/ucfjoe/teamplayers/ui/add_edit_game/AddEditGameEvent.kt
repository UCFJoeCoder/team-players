package com.ucfjoe.teamplayers.ui.add_edit_game

import java.time.LocalDate
import java.time.LocalTime

sealed class AddEditGameEvent {

    data class OnDateChanged(val date: LocalDate) : AddEditGameEvent()

    data class OnTimeChanged(val time: LocalTime) : AddEditGameEvent()

    data object OnSaveGameClick : AddEditGameEvent()

    data object OnShowDatePicker : AddEditGameEvent()

    data object OnHideDatePicker : AddEditGameEvent()

    data object OnShowTimePicker : AddEditGameEvent()

    data object OnHideTimePicker : AddEditGameEvent()

}
