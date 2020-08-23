package com.manudev.cinemaspl.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.manudev.cinemaspl.util.LiveDataCallAdapterFactory
import com.manudev.cinemaspl.util.TestUtil.createCinema
import com.manudev.cinemaspl.util.TestUtil.createMovie
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
class CinemaPLTestUsingMockServer {
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

        val response = (cinemaPLService.getMovies().getOrAwaitValue() as ApiSuccessResponse).body

        val movies = response.data
        assertThat(movies.size, `is`(2))

        val movie = movies[0].movie
        val movieTest = createMovie()
        assertThat(movie.id, `is`(movieTest.id))
        assertThat(movie.title, `is`(movieTest.title))
        assertThat(movie.description, `is`(movieTest.description))
        assertThat(movie.trailerUrl, `is`(movieTest.trailerUrl))
        assertThat(movie.posterUrl, `is`(movieTest.posterUrl))


        val cinemas = movies[0].cinemas
        assertThat(cinemas.size, `is`(2))

        val cinema = cinemas[0]
        val cinemaTest = createCinema()
        assertThat(cinema.id, `is`(cinemaTest.id))
        assertThat(cinema.name, `is`(cinemaTest.name))
        assertThat(cinema.website, `is`(cinemaTest.website))
        assertThat(cinema.logoUrl, `is`(cinemaTest.logoUrl))

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
