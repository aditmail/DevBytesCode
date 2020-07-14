package com.example.devbytesexercice.models

import com.example.devbytesexercice.database.DatabaseVideo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DevByteModel(val videos: List<Video>)

@JsonClass(generateAdapter = true)
data class Video(
    val description: String,
    val thumbnail: String,
    val title: String,
    val updated: String,
    val url: String
)

/** Domain Data **/
//It's Okay to Add this or Not..
/*data class Video(
    val description: String,
    val thumbnail: String,
    val title: String,
    val updated: String,
    val url: String
)*/

fun DevByteModel.asDomainModel(): List<Video> {
    return videos.map {
        Video(
            description = it.description,
            thumbnail = it.thumbnail,
            title = it.title,
            updated = it.updated,
            url = it.url
        )
    }
}

fun DevByteModel.asDatabaseModel(): Array<DatabaseVideo> {
    return videos.map {
        DatabaseVideo(
            description = it.description,
            thumbnail = it.thumbnail,
            title = it.title,
            updated = it.updated,
            url = it.url
        )
    }.toTypedArray()
}