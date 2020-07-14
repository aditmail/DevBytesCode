package com.example.devbytesexercice.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.devbytesexercice.database.DatabaseVideo
import com.example.devbytesexercice.database.VideoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

class DetailVideoViewModel(dataSource: VideoDao, private val keyUrl: String = "") : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = dataSource

    private val detailVideo = MediatorLiveData<DatabaseVideo>()
    fun getVideoDetail() = detailVideo

    init {
        viewDetailVideo()
        Timber.i("URL: $keyUrl")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun viewDetailVideo() {
        detailVideo.addSource(database.getDetailVideo(keyUrl), detailVideo::setValue)
    }

    class Factory(private val dataSource: VideoDao, private val keyUrl: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailVideoViewModel::class.java)) {
                return DetailVideoViewModel(dataSource, keyUrl) as T
            }
            throw IllegalArgumentException("Unable to Find ViewModel")
        }

    }
}