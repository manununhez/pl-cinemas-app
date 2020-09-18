package com.manudev.cinemaspl.api

import androidx.lifecycle.LiveData
import com.manudev.cinemaspl.vo.Attribute
import com.manudev.cinemaspl.vo.FilterAttribute
import com.manudev.cinemaspl.vo.GeneralResponse
import com.manudev.cinemaspl.vo.Movies
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface CinemaPLService {
    @POST("movies/search")
    fun searchMovies(
        @Body filterAttribute: FilterAttribute
    ): LiveData<ApiResponse<GeneralResponse<List<Movies>>>>

    @GET("attributes")
    fun getAttributes(): LiveData<ApiResponse<GeneralResponse<Attribute>>>

}