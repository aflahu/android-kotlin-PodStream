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

    fun xmlDateToDate(dateString: String?): Date {
        val date = dateString ?: return Date()
        val inFormat = SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z",
            Locale.ENGLISH
        ) // https://stackoverflow.com/a/16871410
        return inFormat.parse(date) ?: Date()
    }

    fun dateToShortDate(date: Date): String {
        val outputFormat = DateFormat.getDateInstance(
            DateFormat.SHORT, Locale.getDefault()
        )
        return outputFormat.format(date)
    }

}