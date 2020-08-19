package com.manudev.cinemaspl.repository

import com.manudev.cinemaspl.api.CinemaPLService

class MovieRepository (
    private val cinemaPLService: CinemaPLService
) {
    fun loadMovies() = cinemaPLService.getMovies()

}