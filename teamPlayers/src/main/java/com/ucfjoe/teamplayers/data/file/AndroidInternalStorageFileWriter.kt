package com.ucfjoe.teamplayers.data.file

import android.content.Context
import androidx.core.content.FileProvider
import com.ucfjoe.teamplayers.common.Resource
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class AndroidInternalStorageFileWriter @Inject constructor(
    private val context: Context
) : FileWriter {
    override suspend fun writeFile(fileName: String, byteArray: ByteArray): Resource<String> {
        return saveFile(fileName, byteArray)
    }

    private fun saveFile(fileName: String, byteArray: ByteArray): Resource<String> {
        // Clean up old data
        val directory = File(context.filesDir, "game_files")
        if (!cleanUpDirectoryData(directory)) {
            return Resource.Error("Failed to clean up previous data. File(s) may be locked.")
        }

        // Create the output directory
        if (!directory.exists() && !directory.mkdir() && !directory.mkdirs()) {
            return Resource.Error("Failed to create temp directory.")
        }

        // Write the data to a file
        val file = File(directory, fileName)
        return try {
            FileOutputStream(file).use {
                it.write(byteArray)
            }

            val path = FileProvider.getUriForFile(context, context.applicationContext.packageName+".provider", file).toString()
            Resource.Success(path)
        } catch (e: IOException) {
            Resource.Error("Failed to write temporary file.")
        }
    }

    private fun cleanUpDirectoryData(directory: File): Boolean {
        return deleteDirectory(directory, isRootDirectory = true)
    }

    private fun deleteDirectory(directory: File?, isRootDirectory: Boolean = false): Boolean {
        // Return, without error, if the directory/file is null
        if (directory == null) {
            return true
        }

        // If the passed in object is a file then delete it to clean up the directory
        if (directory.isFile) {
            return directory.delete()
        }

        // If here we are working with a directory. Loop through all of its children. Files and Directories
        val children = directory.list()
        children?.forEach {
            // Make recursive call to delete this file or directory
            if (!deleteDirectory(File(directory, it))) {
                return false
            }
        }
        // If this was the root directory then leave it in place
        if (isRootDirectory) {
            return true
        }
        // Delete non-root directories
        return directory.delete()
    }
}