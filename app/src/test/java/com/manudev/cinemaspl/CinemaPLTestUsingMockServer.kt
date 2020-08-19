package com.manudev.cinemaspl

import com.manudev.cinemaspl.api.CinemaPLService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CinemaPLTestUsingMockServer {
    @get:Rule
    val mockWebServer = MockWebServer()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val cinemaPLService by lazy {
        retrofit.create(CinemaPLService::class.java)

    }


    @Test
    fun getMovies() {
        enqueueResponse("movie/movie_example.json")

        val response = cinemaPLService.getMovies().execute().body()!!

        val movies = response.data
        assertThat(movies.size, `is`(2))

        val movie = movies[0].movie
        assertThat(movie.id, `is`("200"))
        assertThat(movie.title, `is`("Arab Blues"))
        assertThat(movie.description, `is`("description Arab blues"))
        assertThat(movie.trailerUrl, `is`("https://www.youtube.com/watch?v=MKAPlZOH1Xc"))
        assertThat(movie.posterUrl, `is`("https://media.multikino.pl/uploads/images/films_and_events/ab-plakatpl-net-1_ec2187b97a.jpg"))


        val cinemas = movies[0].cinemas
        assertThat(cinemas.size, `is`(2))

        val cinema = cinemas[0]
        assertThat(cinema.id, `is`("2"))
        assertThat(cinema.name, `is`("Cinema City"))
        assertThat(cinema.website, `is`("https://www.cinema-city.pl"))
        assertThat(cinema.logoUrl, `is`("https://www.cinema-city.pl/xmedia/img/10103/logo.svg"))

    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream(fileName)
        val source = Okio.buffer(Okio.source(inputStream))
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
