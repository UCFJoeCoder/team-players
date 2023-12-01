package com.ucfjoe.teamplayers.data

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromTimestamp(dateTimeString: String?): LocalDateTime? {
        if (dateTimeString == null) {
            return null
        }
        return LocalDateTime.parse(dateTimeString)
    }

    @TypeConverter
    fun dateToTimestamp(dateTime: LocalDateTime?): String? {
        return dateTime?.toString()
    }
}