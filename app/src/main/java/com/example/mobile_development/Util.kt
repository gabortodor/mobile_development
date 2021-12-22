package com.example.mobile_development

import java.text.SimpleDateFormat
import java.util.*


fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
    return format.format(date)
}

fun changeColorOpacity(color: Int, op: String): String {
    val hexColor = Integer.toHexString(color)
    return "#$op${hexColor.subSequence(2,hexColor.length)}"
 }
