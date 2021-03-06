package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.Constants
import java.text.SimpleDateFormat
import java.util.*

fun getTodayFormatted() : String {
    val cal = Calendar.getInstance()
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(cal.time)
}

fun getYesterdayFormatted() : String {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DATE, -1)
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(cal.time)
}