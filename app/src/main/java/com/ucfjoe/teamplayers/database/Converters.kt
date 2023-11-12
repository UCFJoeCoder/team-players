package com.ucfjoe.teamplayers.database

import androidx.room.TypeConverter
import java.util.Calendar
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    /**
     * Might not keep this method.. but added it because it could be useful.
     * This is not where it should live if I keep it.
     *
     * @param date
     * @return
     */
    fun getHour(date:Date) : Int
    {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.HOUR_OF_DAY]
    }
}