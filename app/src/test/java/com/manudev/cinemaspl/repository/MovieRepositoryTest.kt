package com.manudev.cinemaspl.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.manudev.cinemaspl.api.CinemaPLService
import com.manudev.cinemaspl.util.ApiUtil.successCall
import com.manudev.cinemaspl.util.TestUtil.createMovies
import com.manudev.cinemaspl.util.mock
import com.manudev.cinemaspl.vo.GeneralResponse
import com.manudev.cinemaspl.vo.Movies
import com.manudev.cinemaspl.vo.Resource
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

    private lateinit var service : CinemaPLService
    private lateinit var repository: MovieRepository

    @Before
    fun setUp() {
        service = mock(CinemaPLService::class.java)
        repository = MovieRepository(service)
    }


    @Test
    fun callFromNetwork_loadMovies() {

        //Correct value passed from service to repo - when success
        val movies = createMovies()
        val call = successCall(GeneralResponse<List<Movies>>(true, "", movies))
        `when`(service.getMovies()).thenReturn(call)

        val observer = mock<Observer<Resource<List<Movies>>>>()

        repository.loadMovies().observeForever(observer)

        verify(service).getMovies() //times(1)
        //Return the correct data transformed: ApiResponse -> Resource
//        verify(observer).onChanged(Resource.loading(null))
        verify(observer).onChanged(Resource.success(movies))
    }
}