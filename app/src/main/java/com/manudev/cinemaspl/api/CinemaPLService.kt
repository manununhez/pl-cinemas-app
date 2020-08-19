package com.manudev.cinemaspl.api

import androidx.lifecycle.LiveData
import retrofit2.http.GET


interface CinemaPLService {
    @GET("movies")
    fun getMovies(): LiveData<ApiResponse<GeneralResponse<List<Movies>>>>

}