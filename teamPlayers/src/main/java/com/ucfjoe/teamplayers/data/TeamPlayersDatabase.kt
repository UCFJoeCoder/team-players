package com.ucfjoe.teamplayers.data

import androidx.room.AutoMigration
import androidx.room.Database
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
    version = 2,
    entities = [GameEntity::class, GamePlayerEntity::class, TeamEntity::class, PlayerEntity::class],
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
@TypeConverters(Converters::class)
abstract class TeamPlayersDatabase : RoomDatabase() {
    abstract val gameDao: GameDao
    abstract val teamDao: TeamDao
    abstract val playerDao: PlayerDao
    abstract val gamePlayerDao: GamePlayerDao
}