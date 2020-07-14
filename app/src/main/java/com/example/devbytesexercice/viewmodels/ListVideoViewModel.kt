package com.example.devbytesexercice.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.devbytesexercice.database.getDatabase
import com.example.devbytesexercice.models.Video
import com.example.devbytesexercice.network.isNetworkAvailable
import com.example.devbytesexercice.repository.VideosRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.IllegalArgumentException

class ListVideoViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val applicationModel = application

    private val database = getDatabase(application)
    private val videosRepository = VideosRepository(database)

    private val _selectedData = MutableLiveData<Video>()
    val selectedData: LiveData<Video>
        get() = _selectedData

    private val _isNetworkActive = MutableLiveData<Boolean>()
    val isNetworkActive: LiveData<Boolean>
        get() = _isNetworkActive

    fun onListVideoClick(video: Video) {
        _selectedData.value = video
    }

    fun onDoneVideoClick() {
        _selectedData.value = null
    }

    //HandlerException for Coroutine
    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Err: $exception")
    }

    init {
        getRefreshVideo()
    }

    fun getRefreshVideo() {
        if (isNetworkAvailable(applicationModel.applicationContext)) {
            viewModelScope.launch(handler) {
                videosRepository.refreshVideos()
            }
            _isNetworkActive.value = true

        } else {
            Timber.i("No Connection Found")
            _isNetworkActive.value = false
        }
    }

    val listVideo = videosRepository.fetchAllVideos

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    //ViewModelFactory
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListVideoViewModel::class.java)) {
                return ListVideoViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to Construct the ViewModel")
        }
    }
}