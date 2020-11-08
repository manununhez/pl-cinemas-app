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

    val mockedFilterAttribute = FilterAttribute("city1", "date1", listOf("Multikino"), listOf("angielski"))

    val mockedCurrentLocation = Coordinate(52.185322, 20.991805)

    val mockedWatchlist = WatchlistMovie(mockedMovie.toDomainMovie())

    val movie_example_json = "{\n" +
            "  \"success\": true,\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "        \"id\": 335,\n" +
            "        \"title\": \"After 2\",\n" +
            "        \"description\": \"Tessa wciąż nie potrafi wybaczyć i zaufać Hardinowi, po tym co między nimi zaszło. Aby wyleczyć się z namiętności, rzuca się w wir pracy.\",\n" +
            "        \"duration\": 105,\n" +
            "        \"genre\": \"|Melodramat\",\n" +
            "        \"original_lang\": \"angielski\",\n" +
            "        \"classification\": \"\",\n" +
            "        \"release_year\": \"2020\",\n" +
            "        \"trailer_url\": \"https://www.youtube.com/watch?v=YoHYAdScBak\",\n" +
            "        \"poster_url\": \"https://media.multikino.pl/uploads/images/films_and_events/after2_dc611831b7.jpg\",\n" +
            "        \"created_at\": \"2020-09-28T19:20:28.000000Z\",\n" +
            "        \"updated_at\": \"2020-09-28T19:23:19.000000Z\",\n" +
            "        \"date_title\": \"2020-10-01\",\n" +
            "        \"city\": \"Warszawa\",\n" +
            "        \"cinemas\": [\n" +
            "        {\n" +
            "          \"cinema_id\": \"1\",\n" +
            "          \"location_id\": \"1\",\n" +
            "          \"name\": \"Warszawa Złote Tarasy\",\n" +
            "          \"coord_latitude\": \"52.229525\",\n" +
            "          \"coord_longitude\": \"21.002011\",\n" +
            "          \"logo_url\": \"https://zakupersi.com/wp-content/uploads/2016/04/multikino-780x390.png\",\n" +
            "          \"cinema_movie_url\": \"https://multikino.pl/filmy/after-2\",\n" +
            "          \"language\": \"polskie napisy\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cinema_id\": \"3\",\n" +
            "          \"location_id\": \"1074\",\n" +
            "          \"name\": \"Warszawa -  Arkadia\",\n" +
            "          \"coord_latitude\": \"52.257217\",\n" +
            "          \"coord_longitude\": \"20.984465\",\n" +
            "          \"logo_url\": \"https://static.antyweb.pl/uploads/2016/02/Cinema_City-1420x670.png\",\n" +
            "          \"cinema_movie_url\": \"https://www.cinema-city.pl/kina/arkadia/1074#/buy-tickets-by-cinema?in-cinema=609&at=2020-10-01&for-movie=4147s2r&view-mode=list\",\n" +
            "          \"language\": \"angielski|polskie napisy\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cinema_id\": \"3\",\n" +
            "          \"location_id\": \"1070\",\n" +
            "          \"name\": \"Warszawa - Galeria Mokotów\",\n" +
            "          \"coord_latitude\": \"52.17884\",\n" +
            "          \"coord_longitude\": \"21.00342\",\n" +
            "          \"logo_url\": \"https://static.antyweb.pl/uploads/2016/02/Cinema_City-1420x670.png\",\n" +
            "          \"cinema_movie_url\": \"https://www.cinema-city.pl/kina/mokotow/1070#/buy-tickets-by-cinema?in-cinema=612&at=2020-10-02&for-movie=4285o2r&view-mode=list\",\n" +
            "          \"language\": \"polski\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cinema_id\": \"3\",\n" +
            "          \"location_id\": \"1068\",\n" +
            "          \"name\": \"Warszawa - Promenada\",\n" +
            "          \"coord_latitude\": \"52.2316\",\n" +
            "          \"coord_longitude\": \"21.106195\",\n" +
            "          \"logo_url\": \"https://static.antyweb.pl/uploads/2016/02/Cinema_City-1420x670.png\",\n" +
            "          \"cinema_movie_url\": \"https://www.cinema-city.pl/kina/promenada/1068#/buy-tickets-by-cinema?in-cinema=614&at=2020-10-02&for-movie=4285o2r&view-mode=list\",\n" +
            "          \"language\": \"polski\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 344,\n" +
            "        \"title\": \"Greenland\",\n" +
            "        \"description\": \"W przededniu katastrofy tylko nieliczni dostają od władz szansę na ocalenie w tajnych schronach. Żeby ją wykorzystać muszą po drodze stoczyć walkę o przetrwanie w świecie, który ogarnął chaos i panika.\",\n" +
            "        \"duration\": 120,\n" +
            "        \"genre\": \"|Science-Fiction|Thriller\",\n" +
            "        \"original_lang\": \"angielski\",\n" +
            "        \"classification\": \"15+\",\n" +
            "        \"release_year\": \"2020\",\n" +
            "        \"trailer_url\": \"https://www.youtube.com/watch?v=ku3oCtmU6jk\",\n" +
            "        \"poster_url\": \"https://media.multikino.pl/uploads/images/films_and_events/greenland-plakat_d46cb8ad29.jpg\",\n" +
            "        \"created_at\": \"2020-09-28T19:20:37.000000Z\",\n" +
            "        \"updated_at\": \"2020-09-28T19:23:19.000000Z\",\n" +
            "        \"date_title\": \"2020-10-01\",\n" +
            "        \"city\": \"Warszawa\",\n" +
            "        \"cinemas\": [\n" +
            "        {\n" +
            "          \"cinema_id\": \"1\",\n" +
            "          \"location_id\": \"14\",\n" +
            "          \"name\": \"Warszawa Targówek\",\n" +
            "          \"coord_latitude\": \"52.302543\",\n" +
            "          \"coord_longitude\": \"21.05757\",\n" +
            "          \"logo_url\": \"https://zakupersi.com/wp-content/uploads/2016/04/multikino-780x390.png\",\n" +
            "          \"cinema_movie_url\": \"https://multikino.pl/filmy/greenland\",\n" +
            "          \"language\": \"polskie napisy\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"message\": \"movies retrieved successfully.\"\n" +
            "}"

    val attribute_example_json = "{\n" +
            "  \"success\": true,\n" +
            "  \"data\": {\n" +
            "    \"cinemas\": [\n" +
            "      \"Cinema City\",\n" +
            "      \"Kino Muranow\",\n" +
            "      \"Kinoteka\",\n" +
            "      \"Multikino\"\n" +
            "    ],\n" +
            "    \"cities\": [\n" +
            "      \"Bielsko-Biała\",\n" +
            "      \"Bydgoszcz\",\n" +
            "      \"Bytom\",\n" +
            "      \"Cieszyn\",\n" +
            "      \"Czechowice-Dziedzice\"\n" +
            "    ],\n" +
            "    \"days\": [\n" +
            "      {\n" +
            "        \"date\": \"2020-11-06\",\n" +
            "        \"movies_available\": true\n" +
            "      },\n" +
            "      {\n" +
            "        \"date\": \"2020-11-07\",\n" +
            "        \"movies_available\": false\n" +
            "      }\n" +
            "    ],\n" +
            "    \"languages\": [\n" +
            "      \"angielski\"\n" +
            "    ]\n" +
            "  },\n" +
            "  \"message\": \"Cities successfully delivered.\"\n" +
            "}"

}