package com.manudev.cinemaspl.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.manudev.cinemaspl.repository.MovieRepository
import com.manudev.cinemaspl.util.TestUtil.createMovies
import com.manudev.cinemaspl.util.mock
import com.manudev.cinemaspl.vo.Movies
import com.manudev.cinemaspl.vo.Resource
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {
    /**
     * InstantTaskExecutorRule is a rule that swaps out
     * that executor and replaces it with synchronous one. This will make sure that, when
     * you're using LiveData with the ViewModel, it's all run synchronously in the tests.
     * this rule basically does is allow us to run LiveData synchronously.
     * This rule is from the core-testing package that was imported earlier.
     */
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(MovieRepository::class.java)
    private var viewModel = MovieViewModel(repository)
    private val loadingObserver = mock<Observer<Boolean>>()
    private val errorObserver = mock<Observer<Boolean>>()
    private val loadTriggerObserver = mock<Observer<Boolean>>()
    private val movieObserver = mock<Observer<Resource<List<Movies>>>>()


    @Before
    fun setup() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.error.observeForever(errorObserver)
        viewModel.loadTrigger.observeForever(loadTriggerObserver)
        viewModel.movies.observeForever(movieObserver)

        viewModel.init()
    }

    @Test
    fun initViewModelSetup_shouldShowLoading_shouldHideError() {
        // shouldShowLoading
        verify(loadingObserver).onChanged(eq(true))

        // shouldHideError
        verify(errorObserver).onChanged(eq(false))

        verifyNoMoreInteractions(repository)
    }

    @Test
    fun resetRefresh_shouldNotLoadMovies(){
        viewModel.resetRefreshMovies()

        //first, loadTrigger is set to true
        verify(loadTriggerObserver).onChanged(false)

        //second, loadMovies is called
        verify(repository, never()).loadMovies()

        verify(movieObserver).onChanged(any())
        verify(errorObserver).onChanged(any())
        verify(loadingObserver).onChanged(any())
    }

    @Test
    fun refresh_shouldLoadMovies() {
        viewModel.refreshMovies()

        //first, loadTrigger is set to true
        verify(loadTriggerObserver).onChanged(true)

        //second, loadMovies is called
        verify(repository).loadMovies()

        //third, loadTrigger is set to false
        verify(loadTriggerObserver).onChanged(false)

        //loadTrigger false should not load movies again
        verifyNoMoreInteractions(repository)

    }

    @Test
    fun refresh_shouldShowLoadingAndHideError_whenLoadMoviesReturnsLoading() {
        val call = setupSuccessResponse(createMovies())
        `when`(repository.loadMovies()).thenReturn(call)

        viewModel.refreshMovies()

        // shouldHideLoading
        verify(loadingObserver).onChanged(eq(true))

        // shouldHideError
        verify(
            errorObserver,
            times(2)
        ).onChanged(eq(false)) //we call two times because init() set error=false the first time

    }

    @Test
    fun refresh_shouldHideLoadingAndError_whenLoadMoviesReturnsSuccess() {
        val call = setupSuccessResponse(createMovies())
        `when`(repository.loadMovies()).thenReturn(call)

        viewModel.refreshMovies()

        verify(repository).loadMovies()

        // shouldHideLoading
        verify(loadingObserver).onChanged(eq(false))

        // shouldHideError
        verify(
            errorObserver,
            times(2)
        ).onChanged(eq(false)) //we call two times because init() set error=false the first time

    }

    @Test
    fun refresh_shouldHideLoadingAndShowError_whenLoadMoviesReturnsError() {
        val call = setupErrorResponse<List<Movies>>()
        `when`(repository.loadMovies()).thenReturn(call)

        viewModel.refreshMovies()

        // shouldHideLoading
        verify(loadingObserver).onChanged(eq(false))

        // shouldHideError
        verify(errorObserver).onChanged(eq(true))
    }


    private fun <T> setupSuccessResponse(data: T) = MutableLiveData<Resource<T>>().apply {
        value = Resource.success(data)
    } as LiveData<Resource<T>>

    private fun <T> setupErrorResponse() = MutableLiveData<Resource<T>>().apply {
        value = Resource.error("Error", null)
    } as LiveData<Resource<T>>

}