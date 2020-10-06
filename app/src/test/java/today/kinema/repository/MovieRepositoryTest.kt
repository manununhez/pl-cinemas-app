package today.kinema.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import today.kinema.api.ApiResponse
import today.kinema.api.CinemaPLService
import today.kinema.db.LocalStorage
import today.kinema.util.AbsentLiveData
import today.kinema.util.ApiUtil.successCall
import today.kinema.util.InstantAppExecutors
import today.kinema.util.TestUtil.createAttributes
import today.kinema.util.TestUtil.createCurrentLocation
import today.kinema.util.TestUtil.createFilterAttribute
import today.kinema.util.TestUtil.createMovies
import today.kinema.util.mock
import today.kinema.vo.Attribute
import today.kinema.vo.GeneralResponse
import today.kinema.vo.Movies
import today.kinema.vo.Resource

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
        val filterAttribute = createFilterAttribute()
        repository.loadMovies(filterAttribute)
        verify(localStorage).getMovies(filterAttribute)
    }

    @Test
    fun loadMovies_fromDb() {
        //----Arrange
        val filterAttribute = createFilterAttribute()
        val movies = createMovies()
        //Prepare DB
        val dbData = MutableLiveData<List<Movies>>()
        dbData.value = movies
        `when`(localStorage.getMovies(filterAttribute)).thenReturn(dbData)

        //----Act
        val observer = mock<Observer<Resource<List<Movies>>>>()
        repository.loadMovies(filterAttribute).observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.success(movies))
    }

    @Test
    fun loadMovies_fromServer() {
        //----Arrange
        val filterAttribute = createFilterAttribute()
        //------
        `when`(localStorage.getCurrentLocation()).thenReturn(createCurrentLocation())
        //Prepare DB
        val dbData = MutableLiveData<List<Movies>>()
        `when`(localStorage.getMovies(filterAttribute)).thenReturn(dbData)
        //Prepare Service
        val movies = createMovies()
        val call = successCall(GeneralResponse(true, "", movies))
        `when`(service.searchMovies(filterAttribute)).thenReturn(call)

        //----Act
        val observer = mock<Observer<Resource<List<Movies>>>>()
        repository.loadMovies(filterAttribute).observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.loading(null))
        //DB updated value
        val updatedDbData = MutableLiveData<List<Movies>>()
        `when`(localStorage.getMovies(filterAttribute)).thenReturn(updatedDbData)

        //Simulate an update in db value, Empty db data, shouldFetch->true
        dbData.postValue(null)
        //createCall()
        verify(service).searchMovies(filterAttribute)
        //saveCallResult
        verify(localStorage).setMovies(movies)
        verify(localStorage).setFilteredAttributes(filterAttribute)
        // send new values to observers
        updatedDbData.postValue(movies)
        verify(observer).onChanged(Resource.success(movies))

    }

    @Test
    fun loadMovies_fromServer_error() {
        //----Arrange
        val filterAttribute = createFilterAttribute()
        //Simulate return empty data from DB
        `when`(localStorage.getMovies(filterAttribute)).thenReturn(AbsentLiveData.create())
        //Prepare Service
        val apiResponse = MutableLiveData<ApiResponse<GeneralResponse<List<Movies>>>>()
        `when`(service.searchMovies(filterAttribute)).thenReturn(apiResponse)

        //----Act
        val observer = mock<Observer<Resource<List<Movies>>>>()
        //Call loadMovies
        repository.loadMovies(filterAttribute).observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))

        //As db return empty value ->ShouldFetch->true
        // Calling server
        //Send error data from server
        apiResponse.postValue(ApiResponse.create(Exception("idk")))

        // send error to observers
        verify(observer).onChanged(Resource.error("idk", null))
    }

    @Test
    fun loadAttributes() {
        repository.loadAttributes()
        verify(localStorage).getAttributes()
    }

    @Test
    fun loadAttributes_fromServer() {
        //----Arrange
        val attributes = createAttributes()
        //Prepare DB
        val dbData = MutableLiveData<Attribute>()
        dbData.value = attributes
        `when`(localStorage.getAttributes()).thenReturn(dbData)

        //Prepare service
        val call = successCall(GeneralResponse(true, "", attributes))
        `when`(service.getAttributes()).thenReturn(call)

        //----Act
        val observer = mock<Observer<Resource<Attribute>>>()
        repository.loadAttributes().observeForever(observer)

        //createCall()
        verify(service).getAttributes()
        //saveCallResult
        verify(localStorage).setAttributes(attributes)

        verify(observer).onChanged(Resource.success(attributes))
    }

    @Test
    fun loadAttributes_fromServer_error() {
        //----Arrange
        //Simulate return empty data from DB
        `when`(localStorage.getAttributes()).thenReturn(AbsentLiveData.create())
        //Prepare Service
        val apiResponse = MutableLiveData<ApiResponse<GeneralResponse<Attribute>>>()
        `when`(service.getAttributes()).thenReturn(apiResponse)

        //----Act
        val observer = mock<Observer<Resource<Attribute>>>()
        //Call loadMovies
        repository.loadAttributes().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))

        //As db return empty value ->ShouldFetch->true
        // Calling server
        //Send error data from server
        apiResponse.postValue(ApiResponse.create(Exception("idk")))

        // send error to observers
        verify(observer).onChanged(Resource.error("idk", null))
    }

    @Test
    fun setAndLoadFilteredAttributes() {
        val filterAttribute = createFilterAttribute()

        repository.setFilteredAttributes(filterAttribute)
        verify(localStorage).setFilteredAttributes(filterAttribute)

        repository.getFilteredAttributes()
        verify(localStorage).getFilteredAttributes()
    }

    @Test
    fun setAndLoadCurrentLocation() {
        val currentLocation = createCurrentLocation()

        repository.setCurrentLocation(currentLocation)
        verify(localStorage).setCurrentLocation(currentLocation)

        repository.getCurrentLocation()
        verify(localStorage).getCurrentLocation()
    }

//    @Test
//    fun orderCinemasByDistance() {
//
//
//        val movies = createMovies()
//        val returnedValue = repository.orderCinemasByDistance(createCurrentLocationEmpty(), movies)
//
//        assertThat(returnedValue, `is`(movies))
//
//        val returnedOrderedValue =
//            repository.orderCinemasByDistance(createCurrentLocation(), createMoviesUnOrdered())
//        `when`(repository.sortCinemas(createMoviesUnOrdered(), createCurrentLocation())).thenReturn(createMoviesOrdered())
//
//
//        assertThat(returnedOrderedValue, `is`(createMoviesOrdered()))
//
//    }
}