package com.ucfjoe.teamplayers.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ucfjoe.teamplayers.database.dao.GameDao
import com.ucfjoe.teamplayers.database.dao.GamePlayerDao
import com.ucfjoe.teamplayers.database.dao.PlayerDao
import com.ucfjoe.teamplayers.database.dao.TeamDao
import com.ucfjoe.teamplayers.database.entity.Game
import com.ucfjoe.teamplayers.database.entity.GamePlayer
import com.ucfjoe.teamplayers.database.entity.Player
import com.ucfjoe.teamplayers.database.entity.Team

@Database(
    entities = [Game::class, GamePlayer::class, Team::class, Player::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TeamPlayersDatabase : RoomDatabase() {

    abstract val gameDao: GameDao
    abstract val teamDao: TeamDao
    abstract val playerDao: PlayerDao
    abstract val gamePlayerDao: GamePlayerDao

    companion object {
        @Volatile
        private var INSTANCE: TeamPlayersDatabase? = null

        fun getInstance(context: Context): TeamPlayersDatabase {
            synchronized(this) {
                return INSTANCE ?: synchronized(this) {
                    Room.databaseBuilder(
                        context.applicationContext,
                        TeamPlayersDatabase::class.java,
                        "team_player_db"
                    ).build().also {
                        INSTANCE = it
                    }
                }
            }
        }
    }
}