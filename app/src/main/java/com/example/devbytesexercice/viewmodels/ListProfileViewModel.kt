package com.example.devbytesexercice.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.devbytesexercice.database.getDatabase
import com.example.devbytesexercice.repository.VideosRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class ListProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val applicationModel = application
    private val database = getDatabase(applicationModel)
    private val videosRepository = VideosRepository(database)

    val listProfile = videosRepository.fetchAllProfile

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListProfileViewModel::class.java)) {
                return ListProfileViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to Construct ViewModel")
        }
    }
}