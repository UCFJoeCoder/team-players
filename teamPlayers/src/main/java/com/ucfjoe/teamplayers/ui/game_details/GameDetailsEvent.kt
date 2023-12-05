package com.ucfjoe.teamplayers.ui.game_details

import com.ucfjoe.teamplayers.domain.model.GamePlayer

sealed class GameDetailsEvent {

    data object OnCancelSelectionClick : GameDetailsEvent()

    data object OnRepeatSelectionClick : GameDetailsEvent()

    data object OnIncrementSelectionClick : GameDetailsEvent()

    data class OnSelectPlayerClick(val player: GamePlayer) : GameDetailsEvent()

    data object OnDismissImportDialog : GameDetailsEvent()

    data object OnImportPlayersRequest : GameDetailsEvent()

    data object OnShowPopupMenu : GameDetailsEvent()

    data object OnHidePopupMenu : GameDetailsEvent()

    data object OnResetCountsToZeroRequest : GameDetailsEvent()

    data object OnShareGameResults : GameDetailsEvent()
}
