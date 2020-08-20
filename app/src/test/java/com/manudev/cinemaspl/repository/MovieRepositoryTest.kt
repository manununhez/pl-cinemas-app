package com.manudev.cinemaspl.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.manudev.cinemaspl.api.CinemaPLService
import com.manudev.cinemaspl.util.ApiUtil.successCall
import com.manudev.cinemaspl.util.TestUtil.createMovies
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val service = mock(CinemaPLService::class.java)

    private lateinit var repository : MovieRepository

    @Before
    fun setUp() {
        repository = MovieRepository(service)
    }

    @Test
    fun loadMovies(){
        //Interactions - Repo call service function once
        repository.loadMovies()
        verify(service).getMovies() //times(1)

        //Correct value passed from service to repo - when success
        `when`(service.getMovies()).thenReturn(successCall(createMovies()))

        assertThat(repository.loadMovies(), Is.`is`(service.getMovies()))
    }
}