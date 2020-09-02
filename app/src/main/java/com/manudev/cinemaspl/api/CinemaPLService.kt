package com.manudev.cinemaspl.api

import androidx.lifecycle.LiveData
import com.manudev.cinemaspl.vo.DayTitle
import com.manudev.cinemaspl.vo.GeneralResponse
import com.manudev.cinemaspl.vo.Location
import com.manudev.cinemaspl.vo.Movies
import retrofit2.http.GET
import retrofit2.http.Query


interface CinemaPLService {
    @GET("movies")
    fun getMovies(@Query("city") query: String): LiveData<ApiResponse<GeneralResponse<List<Movies>>>>

    @GET("locations")
    fun getLocations(): LiveData<ApiResponse<GeneralResponse<List<Location>>>>

    @GET("dates")
    fun getDates(): LiveData<ApiResponse<GeneralResponse<List<DayTitle>>>>

}