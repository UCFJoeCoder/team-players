package com.ucfjoe.teamplayers.dependencyinjection

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ucfjoe.teamplayers.database.TeamPlayersDatabase
import com.ucfjoe.teamplayers.repository.TeamRepository
import com.ucfjoe.teamplayers.repository.TeamRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTeamPlayerDatabase(@ApplicationContext appContext: Context): TeamPlayersDatabase {
        return Room.databaseBuilder(
            appContext,
            TeamPlayersDatabase::class.java,
            "team_player.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTeamRepository(db: TeamPlayersDatabase): TeamRepository {
        return TeamRepositoryImpl(db.teamDao)
    }
}