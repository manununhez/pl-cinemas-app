package com.manudev.cinemaspl.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.manudev.cinemaspl.util.LiveDataCallAdapterFactory
import com.manudev.cinemaspl.util.TestUtil.createDate
import com.manudev.cinemaspl.util.TestUtil.createLocation
import com.manudev.cinemaspl.util.TestUtil.createMovies
import com.manudev.cinemaspl.util.getOrAwaitValue
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

@RunWith(JUnit4::class)
class CinemaPLServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockWebServer = MockWebServer()


    private lateinit var cinemaPLService: CinemaPLService


    @Before
    fun setUp() {
        cinemaPLService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(CinemaPLService::class.java)
    }

    @Test
    fun getMovies() {
        enqueueResponse("movie/movie_example.json")

        val response = (cinemaPLService.getMovies("city1", "date1")
            .getOrAwaitValue() as ApiSuccessResponse).body

        val movies = response.data
        assertThat(movies.size, `is`(2))

        val movie = movies[0].movie
        val movieListTest = createMovies()
        assertThat(movie.id, `is`(movieListTest[0].movie.id))
        assertThat(movie.title, `is`(movieListTest[0].movie.title))
        assertThat(movie.duration, `is`(movieListTest[0].movie.duration))
        assertThat(movie.classification, `is`(movieListTest[0].movie.classification))
        assertThat(movie.releaseYear, `is`(movieListTest[0].movie.releaseYear))
        assertThat(movie.description, `is`(movieListTest[0].movie.description))
        assertThat(movie.dateTitle, `is`(movieListTest[0].movie.dateTitle))
        assertThat(movie.trailerUrl, `is`(movieListTest[0].movie.trailerUrl))
        assertThat(movie.posterUrl, `is`(movieListTest[0].movie.posterUrl))


        val cinemas = movies[0].cinemas
        assertThat(cinemas.size, `is`(2))

        val cinema = cinemas[0]
        assertThat(cinema.cinemaId, `is`(movieListTest[0].cinemas[0].cinemaId))
        assertThat(cinema.name, `is`(movieListTest[0].cinemas[0].name))
        assertThat(cinema.locationId, `is`(movieListTest[0].cinemas[0].locationId))
        assertThat(cinema.latitude, `is`(movieListTest[0].cinemas[0].latitude))
        assertThat(cinema.longitude, `is`(movieListTest[0].cinemas[0].longitude))
        assertThat(cinema.logoUrl, `is`(movieListTest[0].cinemas[0].logoUrl))
        assertThat(cinema.cinemaPageUrl, `is`(movieListTest[0].cinemas[0].cinemaPageUrl))

    }

    @Test
    fun getLocations() {
        enqueueResponse("location/location_example.json")

        val response = (cinemaPLService.getLocations().getOrAwaitValue() as ApiSuccessResponse).body

        val locations = response.data
        assertThat(locations.size, `is`(2))

        val location = locations[0]
        val locationTest = createLocation()
        assertThat(location.city, `is`(locationTest.city))
    }

    @Test
    fun getDates() {
        enqueueResponse("date-title/date_title_example.json")

        val response = (cinemaPLService.getDates().getOrAwaitValue() as ApiSuccessResponse).body

        val dates = response.data
        assertThat(dates.size, `is`(3))

        val dateTitle = dates[0]
        val dateTest = createDate()
        assertThat(dateTitle.date, `is`(dateTest.date))
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
