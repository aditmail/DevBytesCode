package com.example.devbytesexercice.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.devbytesexercice.models.Video

@Entity(tableName = "tb_video")
data class DatabaseVideo constructor(
    @PrimaryKey
    val url: String,
    val description: String,
    val thumbnail: String,
    val title: String,
    val updated: String
)

//Converts from DB Object to Domain Object?
fun List<DatabaseVideo>.asDomainModel(): List<Video> {
    return map {
        Video(
            url = it.url,
            title = it.title,
            description = it.description,
            updated = it.updated,
            thumbnail = it.thumbnail
        )
    }
}

/** User Profile Table **/
@Entity(tableName = "tb_profile")
data class DatabaseProfile constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val username: String,
    val email: String?,
    val phoneNumber: String,
    val password: String,
    val photo: String?
)
/** User Profile Table **/