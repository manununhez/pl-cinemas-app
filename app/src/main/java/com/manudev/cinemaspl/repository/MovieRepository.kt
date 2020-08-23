package com.manudev.cinemaspl.repository

import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource
) {

    fun loadMovies() = remoteDataSource.getMovies()

}