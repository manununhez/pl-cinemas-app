package com.manudev.cinemaspl.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepositoryRetriever {
    private val service: CinemaPLService

    companion object {
        //1
        const val BASE_URL = "http://127.0.0.1:8000/api/" //testing
    }

    init {
        // 2
        val retrofit = Retrofit.Builder()
            // 1
            .baseUrl(BASE_URL)
            //3
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //4
        service = retrofit.create(CinemaPLService::class.java)
    }


}