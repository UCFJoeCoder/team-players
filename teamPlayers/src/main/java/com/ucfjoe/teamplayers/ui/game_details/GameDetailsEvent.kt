package com.ucfjoe.teamplayers.ui.game_details

import com.ucfjoe.teamplayers.domain.model.GamePlayer

sealed class GameDetailsEvent {

    data class OnSelectPlayerClick(val player: GamePlayer) : GameDetailsEvent()

    data object OnCancelSelectionClick : GameDetailsEvent()
    data object OnRepeatSelectionClick : GameDetailsEvent()
    data object OnIncrementSelectionClick : GameDetailsEvent()

    data class OnEditPlayerClick(val player: GamePlayer) : GameDetailsEvent()
    data object OnHideEditPlayerDialog : GameDetailsEvent()
    data class OnProcessEditPlayerRequest(val editPlayer: GamePlayer) : GameDetailsEvent()

    //data object OnRequestImportPlayers : GameDetailsEvent()
    data object OnDismissImportDialog : GameDetailsEvent()
    data object OnImportPlayers : GameDetailsEvent()

    data object OnShowPopupMenu : GameDetailsEvent()
    data object OnHidePopupMenu : GameDetailsEvent()

    data object OnShowShareGameResultsDialog : GameDetailsEvent()
    data object OnHideShareGameResultsDialog : GameDetailsEvent()

    data object OnShowHelpDialog : GameDetailsEvent()
    data object OnHideHelpDialog : GameDetailsEvent()

    data object OnShowResetCountDialog : GameDetailsEvent()
    data object OnHideResetCountDialog : GameDetailsEvent()
    data object OnResetCountsToZero : GameDetailsEvent()
}
