package com.ucfjoe.teamplayers.domain.use_case

import com.ucfjoe.teamplayers.domain.repository.WriteFileRepository
import com.ucfjoe.teamplayers.domain.use_case.common.SendGameEmailUseCase
import com.ucfjoe.teamplayers.domain.use_case.game_players.EditGamePlayerUseCase
import com.ucfjoe.teamplayers.domain.use_case.game_players.GetDifferencesBetweenPlayersAndGamePlayers
import com.ucfjoe.teamplayers.domain.use_case.game_players.ImportPlayersIntoGamePlayersUseCase
import com.ucfjoe.teamplayers.domain.use_case.game_players.IncrementSelectedGamePlayersUseCase
import com.ucfjoe.teamplayers.domain.use_case.game_players.ResetCountsToZeroUseCase
import com.ucfjoe.teamplayers.domain.use_case.games.GetGameWithGamePlayersUseCase
import com.ucfjoe.teamplayers.domain.use_case.games.UpdateGameUseCase
import com.ucfjoe.teamplayers.domain.use_case.teams.GetTeamWithGamesUseCase
import javax.inject.Inject

data class GameDetailsUseCases @Inject constructor(
    val getTeamWithGamesUseCase: GetTeamWithGamesUseCase,
    val getGameWithGamePlayersUseCase: GetGameWithGamePlayersUseCase,
    val editGamePlayerUseCase: EditGamePlayerUseCase,
    val importPlayersIntoGamePlayersUseCase: ImportPlayersIntoGamePlayersUseCase,
    val incrementSelectedGamePlayersUseCase: IncrementSelectedGamePlayersUseCase,
    val resetCountsToZeroUseCase: ResetCountsToZeroUseCase,
    val getDifferencesBetweenPlayersAndGamePlayers: GetDifferencesBetweenPlayersAndGamePlayers,
    val updateGameUseCase: UpdateGameUseCase,
    val writeFileRepository: WriteFileRepository,
    val sendGameEmailUseCase: SendGameEmailUseCase
)
