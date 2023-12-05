package com.ucfjoe.teamplayers.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.ucfjoe.teamplayers.data.TeamPlayersDatabase
import com.ucfjoe.teamplayers.data.repository.GamePlayerRepositoryImpl
import com.ucfjoe.teamplayers.data.repository.GameRepositoryImpl
import com.ucfjoe.teamplayers.data.repository.PlayerRepositoryImpl
import com.ucfjoe.teamplayers.data.repository.TeamRepositoryImpl
import com.ucfjoe.teamplayers.domain.repository.GamePlayerRepository
import com.ucfjoe.teamplayers.domain.repository.GameRepository
import com.ucfjoe.teamplayers.domain.repository.PlayerRepository
import com.ucfjoe.teamplayers.domain.repository.TeamRepository
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

    @Provides
    @Singleton
    fun providePlayerRepository(db: TeamPlayersDatabase): PlayerRepository {
        return PlayerRepositoryImpl(db.playerDao)
    }

    @Provides
    @Singleton
    fun provideGameRepository(db: TeamPlayersDatabase): GameRepository {
        return GameRepositoryImpl(db.gameDao)
    }

    @Provides
    @Singleton
    fun providesGamePlayerRepository(db: TeamPlayersDatabase): GamePlayerRepository {
        return GamePlayerRepositoryImpl(db.gamePlayerDao)
    }
}