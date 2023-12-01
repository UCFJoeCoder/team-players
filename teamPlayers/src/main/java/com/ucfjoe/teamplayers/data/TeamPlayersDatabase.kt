package com.ucfjoe.teamplayers.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ucfjoe.teamplayers.data.local.dao.GameDao
import com.ucfjoe.teamplayers.data.local.dao.GamePlayerDao
import com.ucfjoe.teamplayers.data.local.dao.PlayerDao
import com.ucfjoe.teamplayers.data.local.dao.TeamDao
import com.ucfjoe.teamplayers.data.local.entity.GameEntity
import com.ucfjoe.teamplayers.data.local.entity.GamePlayerEntity
import com.ucfjoe.teamplayers.data.local.entity.PlayerEntity
import com.ucfjoe.teamplayers.data.local.entity.TeamEntity

@Database(
    version = 1,
    entities = [GameEntity::class, GamePlayerEntity::class, TeamEntity::class, PlayerEntity::class],
    exportSchema = true
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