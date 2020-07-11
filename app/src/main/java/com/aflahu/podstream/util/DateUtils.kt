package com.aflahu.podstream.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

object DateUtils {
    fun jsonDateToShortDate(jsonDate: String?): String {
        if (jsonDate == null) {
            "-"
        }

        val inFormat = SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        val date = inFormat.parse(jsonDate) ?: return "-"

        val outputFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())

        return outputFormat.format(date)
    }

}