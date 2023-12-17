package com.ucfjoe.teamplayers.dependencyinjection

import android.content.Context
import com.ucfjoe.teamplayers.data.file.AndroidInternalStorageFileWriter
import com.ucfjoe.teamplayers.data.file.FileWriter
import com.ucfjoe.teamplayers.data.file.WriteFileRepositoryImpl
import com.ucfjoe.teamplayers.data.file.converter.DataConverter
import com.ucfjoe.teamplayers.data.file.converter.DataConverterCsv
import com.ucfjoe.teamplayers.domain.repository.WriteFileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExportModule {

    @Provides
    @Singleton
    fun providesFileWriter(@ApplicationContext context: Context): FileWriter {
        return AndroidInternalStorageFileWriter(context)
    }

    @Provides
    @Singleton
    fun providesDataConverter(): DataConverter {
        return DataConverterCsv()
    }

    @Provides
    @Singleton
    fun providesWriteFileRepository(
        fileWriter: FileWriter,
        dataConverter: DataConverter
    ): WriteFileRepository {
        return WriteFileRepositoryImpl(fileWriter, dataConverter)
    }

}