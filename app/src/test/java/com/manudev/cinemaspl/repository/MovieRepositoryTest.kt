package com.manudev.cinemaspl.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.manudev.cinemaspl.api.ApiResponse
import com.manudev.cinemaspl.api.CinemaPLService
import com.manudev.cinemaspl.db.LocalStorage
import com.manudev.cinemaspl.util.AbsentLiveData
import com.manudev.cinemaspl.util.ApiUtil.successCall
import com.manudev.cinemaspl.util.InstantAppExecutors
import com.manudev.cinemaspl.util.TestUtil.createDates
import com.manudev.cinemaspl.util.TestUtil.createLocations
import com.manudev.cinemaspl.util.TestUtil.createMovies
import com.manudev.cinemaspl.util.mock
import com.manudev.cinemaspl.vo.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var service: CinemaPLService

    @Mock
    private lateinit var localStorage: LocalStorage

    private lateinit var repository: MovieRepository

    @Before
    fun setUp() {
        repository = MovieRepository(service, localStorage, InstantAppExecutors())
    }

    @Test
    fun loadMovies() {
        repository.loadMovies("city", "date")
        verify(localStorage).getMovies("city", "date")
    }

    @Test
    fun loadMovies_fromDb() {
        //----Arrange
        val movies = createMovies()
        //Prepare DB
        val dbData = MutableLiveData<List<Movies>>()
        dbData.value = movies
        `when`(localStorage.getMovies("city", "date")).thenReturn(dbData)


        //----Act
        val observer = mock<Observer<Resource<List<Movies>>>>()
        repository.loadMovies("city", "date").observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.success(movies))
    }

    @Test
    fun loadMovies_fromServer() {
        //----Arrange
        //Prepare DB
        val dbData = MutableLiveData<List<Movies>>()
        `when`(localStorage.getMovies("city", "date")).thenReturn(dbData)
        //Prepare Service
        val movies = createMovies()
        val call = successCall(GeneralResponse(true, "", movies))
        `when`(service.getMovies("city", "date")).thenReturn(call)

        //----Act
        val observer = mock<Observer<Resource<List<Movies>>>>()
        repository.loadMovies("city", "date").observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.loading(null))
        //DB updated value
        val updatedDbData = MutableLiveData<List<Movies>>()
        `when`(localStorage.getMovies("city", "date")).thenReturn(updatedDbData)

        //Simulate an update in db value, Empty db data, shouldFetch->true
        dbData.postValue(null)
        //createCall()
        verify(service).getMovies("city", "date")
        //saveCallResult
        verify(localStorage).setMovies(movies)
        // send new values to observers
        updatedDbData.postValue(movies)
        verify(observer).onChanged(Resource.success(movies))

    }

    @Test
    fun loadMovies_fromServer_error() {
        //----Arrange
        //Simulate return empty data from DB
        `when`(localStorage.getMovies("city", "date")).thenReturn(AbsentLiveData.create())
        //Prepare Service
        val apiResponse = MutableLiveData<ApiResponse<GeneralResponse<List<Movies>>>>()
        `when`(service.getMovies("city", "date")).thenReturn(apiResponse)

        //----Act
        val observer = mock<Observer<Resource<List<Movies>>>>()
        //Call loadMovies
        repository.loadMovies("city", "date").observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))

        //As db return empty value ->ShouldFetch->true
        // Calling server
        //Send error data from server
        apiResponse.postValue(ApiResponse.create(Exception("idk")))

        // send error to observers
        verify(observer).onChanged(Resource.error("idk", null))
    }

    @Test
    fun loadLocations() {
        repository.loadLocations()
        verify(localStorage).getLocations()
    }

    @Test
    fun loadLocations_fromServer() {
        //----Arrange
        val locations = createLocations()
        //Prepare DB
        val dbData = MutableLiveData<List<Location>>()
        dbData.value = locations
        `when`(localStorage.getLocations()).thenReturn(dbData)


        //----Act
        val observer = mock<Observer<Resource<List<Location>>>>()
        repository.loadLocations().observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.success(locations))
    }

    @Test
    fun loadLocations_fromServer_error() {
        //----Arrange
        //Simulate return empty data from DB
        `when`(localStorage.getLocations()).thenReturn(AbsentLiveData.create())
        //Prepare Service
        val apiResponse = MutableLiveData<ApiResponse<GeneralResponse<List<Location>>>>()
        `when`(service.getLocations()).thenReturn(apiResponse)

        //----Act
        val observer = mock<Observer<Resource<List<Location>>>>()
        //Call loadMovies
        repository.loadLocations().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))

        //As db return empty value ->ShouldFetch->true
        // Calling server
        //Send error data from server
        apiResponse.postValue(ApiResponse.create(Exception("idk")))

        // send error to observers
        verify(observer).onChanged(Resource.error("idk", null))
    }

    @Test
    fun loadLocations_fromDb() {
        //----Arrange
        val locations = createLocations()
        //Prepare DB
        val dbData = MutableLiveData<List<Location>>()
        dbData.value = locations
        `when`(localStorage.getLocations()).thenReturn(dbData)


        //----Act
        val observer = mock<Observer<Resource<List<Location>>>>()
        repository.loadLocations().observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.success(locations))
    }

    @Test
    fun loadDates() {
        repository.loadDates()
        verify(localStorage).getDatesTitle()
    }

    @Test
    fun loadDates_fromServer() {
        //----Arrange
        val dates = createDates()
        //Prepare DB
        val dbData = MutableLiveData<List<DateTitle>>()
        dbData.value = dates
        `when`(localStorage.getDatesTitle()).thenReturn(dbData)


        //----Act
        val observer = mock<Observer<Resource<List<DateTitle>>>>()
        repository.loadDates().observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.success(dates))
    }

    @Test
    fun loadDates_fromServer_error() {
        //----Arrange
        //Simulate return empty data from DB
        `when`(localStorage.getDatesTitle()).thenReturn(AbsentLiveData.create())
        //Prepare Service
        val apiResponse = MutableLiveData<ApiResponse<GeneralResponse<List<DateTitle>>>>()
        `when`(service.getDates()).thenReturn(apiResponse)

        //----Act
        val observer = mock<Observer<Resource<List<DateTitle>>>>()
        //Call loadMovies
        repository.loadDates().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))

        //As db return empty value ->ShouldFetch->true
        // Calling server
        //Send error data from server
        apiResponse.postValue(ApiResponse.create(Exception("idk")))

        // send error to observers
        verify(observer).onChanged(Resource.error("idk", null))
    }

    @Test
    fun loadDates_fromDb() {
        //----Arrange
        val dates = createDates()
        //Prepare DB
        val dbData = MutableLiveData<List<DateTitle>>()
        dbData.value = dates
        `when`(localStorage.getDatesTitle()).thenReturn(dbData)


        //----Act
        val observer = mock<Observer<Resource<List<DateTitle>>>>()
        repository.loadDates().observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.success(dates))
    }
}