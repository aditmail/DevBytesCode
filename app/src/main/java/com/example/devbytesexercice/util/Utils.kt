package com.example.devbytesexercice.util

import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.util.Patterns
import com.example.devbytesexercice.R
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

fun getYoutubeIntent(url: String?): Uri {
    val httpUri = Uri.parse(url)
    return Uri.parse("vnd.youtube:" + httpUri.getQueryParameter("v"))
}

fun getCodeYoutube(url: String): Uri {
    val httpUri = Uri.parse(url)
    return Uri.parse(httpUri.getQueryParameter("v"))
}

fun yotubeEmbedded(embedCode: String): String {
    return "<iframe width=\"100%\" height=\"100%\" " +
            "src=\"https://www.youtube.com/embed/" +
            embedCode + "\"" +
            "frameborder=\"0\"" +
            "allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" " +
            "allowfullscreen></iframe>"
}

fun convertDate(date: String, resources: Resources): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val parseDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        resources.getString(
            R.string.updated_at,
            parseDate.format(DateTimeFormatter.ofPattern("dd MM yyyy HH:mm"))
        )
    } else {
        val parseDate = SimpleDateFormat("yyyyMMdd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

        resources.getString(
            R.string.updated_at,
            try {
                formatter.format(parseDate.parse(date))
            } catch (e: ParseException) {
                Timber.i("Error In Parsing: $e")
                val anotherParseDate =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                formatter.format(anotherParseDate.parse(date))
            }
        )
    }
}

fun isEmailValid(emailAddress: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
}