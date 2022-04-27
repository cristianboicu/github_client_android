package com.cristianboicu.githubclient.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun formatDate(date: String): String {
//    2021-01-13T19:32:13Z"
    var parsedDate = date
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//    inputFormat.timeZone = TimeZone.getTigit mergemeZone("UTC")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val date = inputFormat.parse(date)
        parsedDate = outputFormat.format(date)
    } catch (e: Exception) {

    }
    return parsedDate
}

fun convertBooleanToPrivateOrPublic(input: Boolean): String {
    return when (input) {
        true -> "private"
        false -> "public"
    }
}