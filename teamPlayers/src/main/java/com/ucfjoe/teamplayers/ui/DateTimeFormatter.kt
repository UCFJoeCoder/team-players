package com.ucfjoe.teamplayers.ui

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDateTime.formatLocalizedDateTime(
    dateFormatStyle: FormatStyle = FormatStyle.MEDIUM,
    timeFormatStyle: FormatStyle = FormatStyle.SHORT
): String {
    return this.format(DateTimeFormatter.ofLocalizedDateTime(dateFormatStyle, timeFormatStyle))
}

fun LocalDate.formatLocalizedDate(dateFormatStyle: FormatStyle = FormatStyle.LONG): String {
    return this.format(DateTimeFormatter.ofLocalizedDate(dateFormatStyle))
}

fun LocalTime.formatLocalizedTime(timeFormatStyle: FormatStyle = FormatStyle.SHORT): String {
    return this.format(DateTimeFormatter.ofLocalizedTime(timeFormatStyle))
}
