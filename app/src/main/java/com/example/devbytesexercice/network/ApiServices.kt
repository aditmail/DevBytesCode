package com.example.devbytesexercice.network

import com.example.devbytesexercice.models.DevByteModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

/** Define the DevBytesAPIServices **/
interface DevBytesApiService {
    @GET("devbytes.json")
    fun getDevBytesList(): Deferred<DevByteModel>
}

object DevBytesApi {
    val retrofitService: DevBytesApiService by lazy {
        retrofit.create(DevBytesApiService::class.java)
    }
}
/** Define the DevBytesAPIServices **/
