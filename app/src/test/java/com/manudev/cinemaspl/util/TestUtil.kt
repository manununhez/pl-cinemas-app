package com.manudev.cinemaspl.util

import com.manudev.cinemaspl.vo.*

object TestUtil {
    private fun createCinema() = Cinema(
        "1",
        "1",
        "Warszawa Złote Tarasy",
        "polskie napisy",
        "52.229525",
        "21.002011",
        "https://zakupersi.com/wp-content/uploads/2016/04/multikino-780x390.png",
        "https://multikino.pl/filmy/after-2"
    )

    private fun createMovie() = Movie(
        "335",
        "After 2",
        "Tessa wciąż nie potrafi wybaczyć i zaufać Hardinowi, po tym co między nimi zaszło. Aby wyleczyć się z namiętności, rzuca się w wir pracy.",
        "angielski",
        "105",
        "",
        "|Melodramat",
        "2020",
        "2020-10-01",
        "Warszawa",
        "https://www.youtube.com/watch?v=YoHYAdScBak",
        "https://media.multikino.pl/uploads/images/films_and_events/after2_dc611831b7.jpg"
    )


//    private fun createCinemasUnOrdered() = listOf(
//        Cinema(
//            "1",
//            "1",
//            "Warszawa Złote Tarasy",
//            "polskie napisy",
//            "52.229525",
//            "21.002011",
//            "https://zakupersi.com/wp-content/uploads/2016/04/multikino-780x390.png",
//            "https://multikino.pl/filmy/after-2"
//        ),
//        Cinema(
//            "3",
//            "1074",
//            "Warszawa -  Arkadia",
//            "angielski|polskie napisy",
//            "52.257217",
//            "20.984465",
//            "https://static.antyweb.pl/uploads/2016/02/Cinema_City-1420x670.png",
//            "https://www.cinema-city.pl/kina/arkadia/1074#/buy-tickets-by-cinema?in-cinema=609&at=2020-10-01&for-movie=4147s2r&view-mode=list"
//        ),
//        Cinema(
//            "3",
//            "1070",
//            "Warszawa - Galeria Mokotów",
//            "polski",
//            "52.17884",
//            "21.00342",
//            "https://static.antyweb.pl/uploads/2016/02/Cinema_City-1420x670.png",
//            "https://www.cinema-city.pl/kina/mokotow/1070#/buy-tickets-by-cinema?in-cinema=612&at=2020-10-02&for-movie=4285o2r&view-mode=list"
//        ),
//        Cinema(
//            "3",
//            "1068",
//            "Warszawa - Promenada",
//            "polski",
//            "52.2316",
//            "21.106195",
//            "https://static.antyweb.pl/uploads/2016/02/Cinema_City-1420x670.png",
//            "https://www.cinema-city.pl/kina/promenada/1068#/buy-tickets-by-cinema?in-cinema=614&at=2020-10-02&for-movie=4285o2r&view-mode=list"
//        )
//    )
//
//    private fun createCinemasOrdered() = listOf(
//        Cinema(
//            "3",
//            "1070",
//            "Warszawa - Galeria Mokotów",
//            "polski",
//            "52.17884",
//            "21.00342",
//            "https://static.antyweb.pl/uploads/2016/02/Cinema_City-1420x670.png",
//            "https://www.cinema-city.pl/kina/mokotow/1070#/buy-tickets-by-cinema?in-cinema=612&at=2020-10-02&for-movie=4285o2r&view-mode=list"
//        ),
//        Cinema(
//            "1",
//            "1",
//            "Warszawa Złote Tarasy",
//            "polskie napisy",
//            "52.229525",
//            "21.002011",
//            "https://zakupersi.com/wp-content/uploads/2016/04/multikino-780x390.png",
//            "https://multikino.pl/filmy/after-2"
//        ),
//        Cinema(
//            "3",
//            "1074",
//            "Warszawa -  Arkadia",
//            "angielski|polskie napisy",
//            "52.257217",
//            "20.984465",
//            "https://static.antyweb.pl/uploads/2016/02/Cinema_City-1420x670.png",
//            "https://www.cinema-city.pl/kina/arkadia/1074#/buy-tickets-by-cinema?in-cinema=609&at=2020-10-01&for-movie=4147s2r&view-mode=list"
//        ),
//        Cinema(
//            "3",
//            "1068",
//            "Warszawa - Promenada",
//            "polski",
//            "52.2316",
//            "21.106195",
//            "https://static.antyweb.pl/uploads/2016/02/Cinema_City-1420x670.png",
//            "https://www.cinema-city.pl/kina/promenada/1068#/buy-tickets-by-cinema?in-cinema=614&at=2020-10-02&for-movie=4285o2r&view-mode=list"
//        )
//    )
//
//    fun createMoviesUnOrdered() = listOf(Movies(createMovie(), createCinemasUnOrdered()))
//    fun createMoviesOrdered() = listOf(Movies(createMovie(), createCinemasOrdered()))
    fun createMovies() = listOf(Movies(createMovie(), listOf(createCinema())))

    fun createAttributes() = Attribute(
        listOf(
            "Cinema City",
            "Kino Muranow",
            "Kinoteka",
            "Multikino"
        ),
        listOf(
            "Bielsko-Biała",
            "Bydgoszcz",
            "Bytom",
            "Cieszyn",
            "Czechowice-Dziedzice"
        ),
        listOf(
            "2020-10-01",
            "2020-10-02",
            "2020-10-03"
        ),
        listOf("angielski")
    )

    fun createFilterAttribute() = FilterAttribute("city1", "date1", listOf(), listOf())

    fun createCurrentLocation() = Coordinate(52.185322, 20.991805)
//    fun createCurrentLocationEmpty() = Coordinate(0.0, 0.0)
}