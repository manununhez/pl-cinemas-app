package today.kinema.util

import today.kinema.vo.*

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