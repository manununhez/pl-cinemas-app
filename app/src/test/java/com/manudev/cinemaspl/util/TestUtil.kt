package com.manudev.cinemaspl.util

import com.manudev.cinemaspl.vo.*

object TestUtil {
    private fun createCinema() = Cinema(
        "2",
        "1074",
        "Warszawa -  Arkadia",
        "52.257217",
        "20.984465",
        "https://www.cinema-city.pl/loga/cc/Cinema_City_Master_RGB_blackBg.png",
        "https://www.cinema-city.pl/filmy/zieja/4074s2r"
    )

    private fun createCinemas() = listOf(createCinema())

    private fun createMovie() = Movie(
        "2090",
        "Zieja",
        "Lata 70-te. Major SB chce \"kupić\" naiwnego księdza Zieję i zrobić z niego agenta, który skompromituje opozycję. Przesłuchania księdza stają się naturalnym pretekstem do wędrówki przez historię Polski wieku dwudziestego - od wojny bolszewickiej 1920 roku, przez II wojnę światową, aż do czasów współczesnych.",
        "111",
        "12+",
        "2019",
        "2020-09-08",
        "https://www.youtube.com/watch?v=g-mwqHq_3Og&t=9s",
        "https://media.multikino.pl/uploads/images/films_and_events/zieja-poster_f70c7c06fd.jpg",
    )

    fun createMovies() = listOf(Movies(createMovie(), createCinemas()))

    fun createLocation() = Location("Bielsko-Biała")

    fun createLocations() = listOf(createLocation(), createLocation())

    fun createDate() = DateTitle("2020-09-08")

    fun createDates() = listOf(createDate(), createDate())

}