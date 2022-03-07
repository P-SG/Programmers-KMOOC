package com.programmers.kmooc.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun parseDate(dateString: String): Date {
        try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            return format.parse(dateString)
        } catch (e: Exception) {
            return Date()
        }
    }

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("yyyy/MM/dd")
        return format.format(date)
    }

    fun parseString(start:Date, end:Date) = "${formatDate(start)} ~ ${formatDate(end)}"
}