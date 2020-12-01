package today.kinema

import today.kinema.data.api.model.Cinema
import today.kinema.data.api.model.Movie

object TestUtil {
    val mockedCinema = Cinema(
        "1",
        "1",
        "Warszawa Złote Tarasy",
        "polskie napisy",
        "52.229525",
        "21.002011",
        "https://zakupersi.com/wp-content/uploads/2016/04/multikino-780x390.png",
        "https://multikino.pl/filmy/after-2"
    )

     val mockedMovie = Movie(
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
}