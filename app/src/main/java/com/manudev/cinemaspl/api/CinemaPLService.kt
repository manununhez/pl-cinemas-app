package com.manudev.cinemaspl.api

import retrofit2.Call
import retrofit2.http.GET


interface CinemaPLService {
    @GET("movies")
    fun getMovies(): Call<ResponseMovie>

}