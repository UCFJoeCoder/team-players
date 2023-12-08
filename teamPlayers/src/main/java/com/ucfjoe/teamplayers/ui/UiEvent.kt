package com.ucfjoe.teamplayers.ui

import android.widget.Toast

sealed class UiEvent {

    data class ShowToast(
        val message: String,
        val duration: Int = Toast.LENGTH_SHORT
    ) : UiEvent()

    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ) : UiEvent()
}