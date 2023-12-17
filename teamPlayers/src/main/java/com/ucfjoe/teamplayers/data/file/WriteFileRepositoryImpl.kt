package com.ucfjoe.teamplayers.data.file

import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.data.file.converter.DataConverter
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.model.Team
import com.ucfjoe.teamplayers.domain.repository.WriteFileRepository
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class WriteFileRepositoryImpl @Inject constructor(
    private val fileWriter: FileWriter,
    private val csvDataConverter: DataConverter

) : WriteFileRepository {
    override suspend fun writeFile(
        team: Team,
        game: Game,
        player: List<GamePlayer>
    ): Resource<String> {
        csvDataConverter.convertData(team, game, player).let {result ->
            when (result) {
                is Resource.Success -> {
                    result.data.byteArray.let {
                        val fileName = buildFileName(team, game)
                        return when (val fileResult = fileWriter.writeFile(fileName, it)){
                            is Resource.Success -> {
                                Resource.Success(fileResult.data)
                            }

                            is Resource.Error -> {
                                Resource.Error(fileResult.message)
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    return Resource.Error(result.message)
                }
            }
        }
    }

    private fun buildFileName(team: Team, game: Game): String {

        val fileNameDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val fileNameDate = game.gameDateTime.format(fileNameDateFormatter)

        val teamName = team.name.replace(regex = Regex("[^A-Za-z0-9]"), replacement = "")

        return "$teamName-$fileNameDate.csv"
    }
}