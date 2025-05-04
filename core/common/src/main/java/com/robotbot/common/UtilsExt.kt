package com.robotbot.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Suppress("NewApi")
fun LocalDate.toFullString(): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
    return format(formatter)
}
