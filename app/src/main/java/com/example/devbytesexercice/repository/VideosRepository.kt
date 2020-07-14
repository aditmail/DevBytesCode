package com.example.devbytesexercice.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.devbytesexercice.database.DatabaseProfile
import com.example.devbytesexercice.database.DevBytesDatabase
import com.example.devbytesexercice.database.asDomainModel
import com.example.devbytesexercice.models.Video
import com.example.devbytesexercice.models.asDatabaseModel
import com.example.devbytesexercice.network.DevBytesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class VideosRepository(private val database: DevBytesDatabase) {

    val fetchAllVideos: LiveData<List<Video>> =
        Transformations.map(database.videoDAO.getListVideos()) {
            it.asDomainModel() //View The Data from Database as Domain Model.. or JSON?
        }

    val fetchAllProfile: LiveData<List<DatabaseProfile>> =
        database.profileDAO.getListProfile()

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            try {
                val getListVideo = DevBytesApi.retrofitService.getDevBytesList().await()
                Timber.i("Fetch Data: ${getListVideo}")

                database.videoDAO.insertAll(*getListVideo.asDatabaseModel()) //Saving as Database
            } catch (e: HttpException) {
                Timber.i("Error Conn: $e")
            }
        }
    }
}