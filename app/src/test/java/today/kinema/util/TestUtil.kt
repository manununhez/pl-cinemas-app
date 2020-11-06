package today.kinema.util

import today.kinema.data.api.model.*
import today.kinema.data.toDomainMovie
import today.kinema.vo.Coordinate
import today.kinema.vo.WatchlistMovie

object TestUtil {
    private val mockedCinema = Cinema(
        "1",
        "1",
        "Warszawa Złote Tarasy",
        "polskie napisy",
        "52.229525",
        "21.002011",
        "https://zakupersi.com/wp-content/uploads/2016/04/multikino-780x390.png",
        "https://multikino.pl/filmy/after-2"
    )

    private val mockedMovie = Movie(
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
        "https://media.multikino.pl/uploads/images/films_and_events/after2_dc611831b7.jpg",
        listOf(mockedCinema)
    )

    val mockedMovies = listOf(mockedMovie)

    val mockedAttributes = Attribute(
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
            Day("2020-11-06", true),
            Day("2020-11-07", false)
        ),
        listOf("angielski")
    )

    val mockedFilterAttribute = FilterAttribute("city1", "date1", listOf(), listOf())

    val mockedCurrentLocation = Coordinate(52.185322, 20.991805)

    val mockedWatchlist = WatchlistMovie(mockedMovie.toDomainMovie())

}