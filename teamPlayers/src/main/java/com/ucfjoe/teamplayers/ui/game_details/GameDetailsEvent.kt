package com.ucfjoe.teamplayers.ui.game_details

import com.ucfjoe.teamplayers.domain.model.GamePlayer

sealed class GameDetailsEvent {

    data object OnCancelSelectionClick : GameDetailsEvent()

    data object OnRepeatSelectionClick : GameDetailsEvent()

    data object OnIncrementSelectionClick : GameDetailsEvent()

    data class OnSelectPlayerClick(val player: GamePlayer) : GameDetailsEvent()

    data object OnDismissImportDialog : GameDetailsEvent()

    data object OnImportPlayers : GameDetailsEvent()

    data object OnRequestImportPlayers : GameDetailsEvent()

    data object OnShowPopupMenu : GameDetailsEvent()

    data object OnHidePopupMenu : GameDetailsEvent()

    data object OnResetCountsToZero : GameDetailsEvent()

    data object OnShareGameResults : GameDetailsEvent()

    data object OnHideShareGameResults : GameDetailsEvent()

    data object OnShowHelpDialog : GameDetailsEvent()

    data object OnHideHelpDialog : GameDetailsEvent()

    data object OnShowRequestClearCountDialog: GameDetailsEvent()

    data object OnHideRequestClearCountDialog: GameDetailsEvent()
}
