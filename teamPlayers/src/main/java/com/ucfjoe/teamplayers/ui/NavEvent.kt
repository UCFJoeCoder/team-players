package com.ucfjoe.teamplayers.ui

sealed class NavEvent {
    data object PopBackStack: NavEvent()
    data class Navigate(val route: String): NavEvent()
}
