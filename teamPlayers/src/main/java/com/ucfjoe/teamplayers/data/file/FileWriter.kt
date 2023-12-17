package com.ucfjoe.teamplayers.data.file

import com.ucfjoe.teamplayers.common.Resource

fun interface FileWriter {
    suspend fun writeFile(fileName:String, byteArray: ByteArray): Resource<String>

}