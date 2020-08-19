package com.manudev.cinemaspl.util

import com.manudev.cinemaspl.api.Cinema
import com.manudev.cinemaspl.api.GeneralResponse
import com.manudev.cinemaspl.api.Movie
import com.manudev.cinemaspl.api.Movies

object TestUtil {
    fun createCinema() = Cinema(
        "2",
        "Cinema City",
        "https://www.cinema-city.pl",
        "https://www.cinema-city.pl/xmedia/img/10103/logo.svg"
    )

    private fun createCinemas() = listOf(createCinema())

    fun createMovie() = Movie(
        "200",
        "Arab Blues",
        "description Arab blues",
        "https://www.youtube.com/watch?v=MKAPlZOH1Xc",
        "https://media.multikino.pl/uploads/images/films_and_events/ab-plakatpl-net-1_ec2187b97a.jpg"
    )

    fun createMovie2() = Movie(
        "201",
        "Arab Blues",
        "description Arab blues",
        "https://www.youtube.com/watch?v=MKAPlZOH1Xc",
        "https://media.multikino.pl/uploads/images/films_and_events/ab-plakatpl-net-1_ec2187b97a.jpg"
    )

    fun createMovies() = GeneralResponse(true, "", listOf(Movies(createMovie(), createCinemas())))

    fun createMovies2() = GeneralResponse(true, "", listOf(Movies(createMovie2(), createCinemas())))

}