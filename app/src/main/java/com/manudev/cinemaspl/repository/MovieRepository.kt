package com.manudev.cinemaspl.repository

import androidx.lifecycle.LiveData
import com.manudev.cinemaspl.api.ApiResponse
import com.manudev.cinemaspl.api.CinemaPLService
import com.manudev.cinemaspl.vo.GeneralResponse
import com.manudev.cinemaspl.vo.Movies

class MovieRepository(
    private val cinemaPLService: CinemaPLService
) {

    fun loadMovies() = object : NetworkBoundResource<List<Movies>>() {
        override fun createCall(): LiveData<ApiResponse<GeneralResponse<List<Movies>>>> =
            cinemaPLService.getMovies()
    }.asLiveData()

}