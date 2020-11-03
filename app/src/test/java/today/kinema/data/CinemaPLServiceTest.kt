package today.kinema.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import today.kinema.data.api.ApiSuccessResponse
import today.kinema.data.api.KinemaService
import today.kinema.util.TestUtil.createAttributes
import today.kinema.util.TestUtil.createFilterAttribute
import today.kinema.util.TestUtil.createMovies

@RunWith(JUnit4::class)
class CinemaPLServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

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

    @Test
    fun getMovies() {
        enqueueResponse("movie/movie_example.json")

        val response = (kinemaService.searchMovies(createFilterAttribute())
            .getOrAwaitValue() as ApiSuccessResponse).body

        val movies = response.data
        assertThat(movies.size, `is`(2))

        val movie = movies[0]
        val movieListTest = createMovies()
        assertThat(movie.id, `is`(movieListTest[0].id))
        assertThat(movie.title, `is`(movieListTest[0].title))
        assertThat(movie.description, `is`(movieListTest[0].description))
        assertThat(movie.originalLanguage, `is`(movieListTest[0].originalLanguage))
        assertThat(movie.duration, `is`(movieListTest[0].duration))
        assertThat(movie.classification, `is`(movieListTest[0].classification))
        assertThat(movie.genre, `is`(movieListTest[0].genre))
        assertThat(movie.releaseYear, `is`(movieListTest[0].releaseYear))
        assertThat(movie.dateTitle, `is`(movieListTest[0].dateTitle))
        assertThat(movie.city, `is`(movieListTest[0].city))
        assertThat(movie.trailerUrl, `is`(movieListTest[0].trailerUrl))
        assertThat(movie.posterUrl, `is`(movieListTest[0].posterUrl))


        val cinemas = movies[0].cinemas
        assertThat(cinemas.size, `is`(4))

        val cinema = cinemas[0]
        assertThat(cinema.cinemaId, `is`(movieListTest[0].cinemas[0].cinemaId))
        assertThat(cinema.locationId, `is`(movieListTest[0].cinemas[0].locationId))
        assertThat(cinema.locationName, `is`(movieListTest[0].cinemas[0].locationName))
        assertThat(cinema.language, `is`(movieListTest[0].cinemas[0].language))
        assertThat(cinema.latitude, `is`(movieListTest[0].cinemas[0].latitude))
        assertThat(cinema.longitude, `is`(movieListTest[0].cinemas[0].longitude))
        assertThat(cinema.logoUrl, `is`(movieListTest[0].cinemas[0].logoUrl))
        assertThat(cinema.cinemaPageUrl, `is`(movieListTest[0].cinemas[0].cinemaPageUrl))

    }

    @Test
    fun getAttributes() {
        enqueueResponse("attribute/attributes_example.json")

        val response = (kinemaService.getAttributes().getOrAwaitValue() as ApiSuccessResponse).body

        val attribute = response.data
        assertThat(attribute.cinemas.size, `is`(4))
        assertThat(attribute.cities.size, `is`(5))
        assertThat(attribute.days.size, `is`(3))
        assertThat(attribute.languages.size, `is`(1))

        val attributeTest = createAttributes()
        assertThat(attribute.cinemas, `is`(attributeTest.cinemas))
        assertThat(attribute.cities, `is`(attributeTest.cities))
        assertThat(attribute.days, `is`(attributeTest.days))
        assertThat(attribute.languages, `is`(attributeTest.languages))
    }


    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
//                .setResponseCode(200))
        )
    }
}
