package com.ucfjoe.teamplayers.data.file.converter

import com.ucfjoe.teamplayers.common.Resource
import com.ucfjoe.teamplayers.domain.model.Game
import com.ucfjoe.teamplayers.domain.model.GamePlayer
import com.ucfjoe.teamplayers.domain.model.Team
import java.time.format.DateTimeFormatter

class DataConverterCsv : DataConverter {
    override fun convertData(
        team: Team,
        game: Game,
        players: List<GamePlayer>
    ): Resource<GamePlayerCsvInfo> {
        val csvDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        val csvHeadings = "Team,Game Date,Jersey Number,Number of Plays,Is Absent"
        val csvData = StringBuilder()
            .append(csvHeadings)
            .append("\r\n")

        val teamName = escapeCsvString(team.name)
        val gameDateTime = game.gameDateTime.format(csvDateFormatter)
        players.forEach { player ->
            with(csvData) {
                append(teamName)
                append(",")
                append(gameDateTime)
                append(",")
                append(player.jerseyNumber)
                append(",")
                append(player.count)
                append(",")
                append(player.isAbsent)
                append("\r\n")
            }
        }
        return Resource.Success(GamePlayerCsvInfo(csvData.toString().toByteArray()))
    }

    private fun escapeCsvString(data: String): String {
        // If the string does not contain a comma or a double quote then return the data
        if (!data.contains("\"") && !data.contains(",")) {
            return data
        }

        // escape the double quotes, if they exist, and wrap the string in double quotes
        val escapedString = data.replace("\"", "\"\"")
        return "\"$escapedString\""
    }
}


