package com.gmail.thanhva.basemvvmproject.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 *  Create by thanhva on 19/08/2020
 *  Class DateUtils
 */
const val TIME_FORMAT_CALENDER = "yyyy/MM/dd"

fun convertDate(date: String, typeDefault: String, typeConvert: String): String {
    val dataParser = SimpleDateFormat(typeDefault, Locale.ENGLISH)
    val output = SimpleDateFormat(typeConvert, Locale.ENGLISH)
    return output.format(dataParser.parse(date) ?: "")
}

fun getToday(): String {
    val df = SimpleDateFormat(
        TIME_FORMAT_CALENDER,
        Locale("en", "en", "en")
    )
    return df.format(Calendar.getInstance().time)
}