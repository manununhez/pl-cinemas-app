package com.manudev.cinemaspl.repository

import androidx.lifecycle.LiveData
import com.manudev.cinemaspl.api.ApiResponse
import com.manudev.cinemaspl.api.CinemaPLService
import com.manudev.cinemaspl.vo.GeneralResponse
import com.manudev.cinemaspl.vo.Location
import com.manudev.cinemaspl.vo.Movies
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val cinemaPLService: CinemaPLService
    ) {
        fun loadMovies(city: String) = object : NetworkBoundResource<List<Movies>>() {
            override fun createCall(): LiveData<ApiResponse<GeneralResponse<List<Movies>>>> =
                cinemaPLService.getMovies(city)
        }.asLiveData()

    fun loadLocations() = object : NetworkBoundResource<List<Location>>() {
        override fun createCall(): LiveData<ApiResponse<GeneralResponse<List<Location>>>> =
            cinemaPLService.getLocations()
    }.asLiveData()
}