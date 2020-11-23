package today.kinema.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import today.kinema.data.api.KinemaService
import today.kinema.util.TestUtil.attribute_example_json
import today.kinema.util.TestUtil.mockedAttributes
import today.kinema.util.TestUtil.mockedFilterAttribute
import today.kinema.util.TestUtil.mockedMovies
import today.kinema.util.TestUtil.movie_example_json

@RunWith(JUnit4::class)
class KinemaServiceTest {
    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var kinemaService: KinemaService

    @Before
    fun setUp() {
        kinemaService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KinemaService::class.java)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `movies search using filter attributes`() = runBlocking {
        enqueueResponse(movie_example_json)

        val response = kinemaService.searchMovies(mockedFilterAttribute)

        val movies = response.data
        assertThat(movies.size, `is`(2))

        val movie = movies[0]
        val movieListTest = mockedMovies[0]
        assertThat(movie.id, `is`(movieListTest.id))
        assertThat(movie.title, `is`(movieListTest.title))
        assertThat(movie.description, `is`(movieListTest.description))
        assertThat(movie.originalLanguage, `is`(movieListTest.originalLanguage))
        assertThat(movie.duration, `is`(movieListTest.duration))
        assertThat(movie.classification, `is`(movieListTest.classification))
        assertThat(movie.genre, `is`(movieListTest.genre))
        assertThat(movie.releaseYear, `is`(movieListTest.releaseYear))
        assertThat(movie.dateTitle, `is`(movieListTest.dateTitle))
        assertThat(movie.city, `is`(movieListTest.city))
        assertThat(movie.trailerUrl, `is`(movieListTest.trailerUrl))
        assertThat(movie.posterUrl, `is`(movieListTest.posterUrl))


        val cinemas = movies[0].cinemas
        assertThat(cinemas.size, `is`(4))

        val cinema = cinemas[0]
        assertThat(cinema.cinemaId, `is`(movieListTest.cinemas[0].cinemaId))
        assertThat(cinema.locationId, `is`(movieListTest.cinemas[0].locationId))
        assertThat(cinema.locationName, `is`(movieListTest.cinemas[0].locationName))
        assertThat(cinema.language, `is`(movieListTest.cinemas[0].language))
        assertThat(cinema.latitude, `is`(movieListTest.cinemas[0].latitude))
        assertThat(cinema.longitude, `is`(movieListTest.cinemas[0].longitude))
        assertThat(cinema.logoUrl, `is`(movieListTest.cinemas[0].logoUrl))
        assertThat(cinema.cinemaPageUrl, `is`(movieListTest.cinemas[0].cinemaPageUrl))

    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get attributes according to filter attributes`() = runBlocking {
        enqueueResponse(attribute_example_json)

        val response = kinemaService.getAttributes(mockedFilterAttribute)

        val attribute = response.data
        assertThat(attribute.cinemas.size, `is`(4))
        assertThat(attribute.cities.size, `is`(5))
        assertThat(attribute.days.size, `is`(2))
        assertThat(attribute.languages.size, `is`(1))

        val attributeTest = mockedAttributes
        assertThat(attribute.cinemas, `is`(attributeTest.cinemas))
        assertThat(attribute.cities, `is`(attributeTest.cities))
        assertThat(attribute.days, `is`(attributeTest.days))
        assertThat(attribute.languages, `is`(attributeTest.languages))
    }


    private fun enqueueResponse(filename: String, headers: Map<String, String> = emptyMap()) {
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(filename))
    }
}
