package com.ucfjoe.teamplayers.dependencyinjection

import android.content.Context
import com.ucfjoe.teamplayers.domain.use_case.common.SendGameEmailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EmailModule {

    @Provides
    @Singleton
    fun providesSendGameEmailUseCase(@ApplicationContext context: Context): SendGameEmailUseCase {
        return SendGameEmailUseCase(context)
    }

}