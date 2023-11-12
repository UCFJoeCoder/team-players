package com.ucfjoe.teamplayers.dependencyinjection

import android.app.Application
import androidx.room.Room
import com.ucfjoe.teamplayers.database.TeamPlayersDatabase
import com.ucfjoe.teamplayers.repository.TeamRepository
import com.ucfjoe.teamplayers.repository.TeamRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTeamPlayerDatabase(app: Application): TeamPlayersDatabase {
        return Room.databaseBuilder(
            app,
            TeamPlayersDatabase::class.java,
            "team_player_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTeamRepository(db: TeamPlayersDatabase): TeamRepository {
        return TeamRepositoryImpl(db.teamDao)
    }

}