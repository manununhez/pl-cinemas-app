package today.kinema.repository

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import today.kinema.data.api.KinemaDataSource
import today.kinema.data.api.Resource
import today.kinema.data.api.model.GeneralResponse
import today.kinema.data.db.RoomDataSource
import today.kinema.data.toDomainAttribute
import today.kinema.data.toDomainFilterAttribute
import today.kinema.data.toDomainMovie
import today.kinema.data.toServerMovie
import today.kinema.util.TestUtil.mockedAttributes
import today.kinema.util.TestUtil.mockedCurrentLocation
import today.kinema.util.TestUtil.mockedFilterAttribute
import today.kinema.util.TestUtil.mockedMovies
import today.kinema.util.TestUtil.mockedWatchlist
import today.kinema.vo.Coordinate
import today.kinema.vo.Movie

class KinemaRepositoryTest {

    private val kinemaDataSource = mockk<KinemaDataSource>()

    private val roomDataSource = mockk<RoomDataSource>()

    private lateinit var repository: KinemaRepository

    @Before
    fun setUp() {
        repository = KinemaRepository(kinemaDataSource, roomDataSource)
    }

    @Test
    fun `load movies from DB without server call`() {
        //----Arrange
        val filterAttribute = mockedFilterAttribute.toDomainFilterAttribute()
        val movies = mockedMovies.map { it.toDomainMovie() }
        val isAsc = true
        //Prepare DB
        coEvery { roomDataSource.isMoviesNotEmpty(isAsc) }.returns(true)
        coEvery { roomDataSource.getMovies(isAsc) }.returns(movies)

        //----Act
        val result = runBlocking { repository.loadMovies(filterAttribute, isAsc) }
        //I want to make sure mocked.x wasn't called anymore
        coVerify(exactly = 0) { kinemaDataSource.searchMovies(filterAttribute) }

        assertEquals(Resource.success(movies), result)
    }

    @Test
    fun `load movies from DB with server call error`() {
        //----Arrange
        val filterAttribute = mockedFilterAttribute.toDomainFilterAttribute()
        val movies = listOf<Movie>()
        val isAsc = true
        val message = ""
        val errorResponse = GeneralResponse(false, message, movies.map { it.toServerMovie() })
        val expectedResult = Resource.error(message, null)

        //Prepare DB
        coEvery { roomDataSource.isMoviesNotEmpty(isAsc) }.returns(false)
        coEvery { kinemaDataSource.searchMovies(filterAttribute) }.returns(errorResponse)

        //----Act
        val result = runBlocking { repository.loadMovies(filterAttribute, isAsc) }
        //I want to make sure mocked.x wasn't called anymore
        coVerify(exactly = 1) {
            kinemaDataSource.searchMovies(filterAttribute)
        }

        coVerifySequence {
            roomDataSource.isMoviesNotEmpty(isAsc)
            kinemaDataSource.searchMovies(filterAttribute)
        }

        assertEquals(expectedResult, result)
    }

    @Test
    fun `load movies from DB with server call success`() {
        //----Arrange
        val filterAttribute = mockedFilterAttribute.toDomainFilterAttribute()
        val movies = mockedMovies.map { it.toDomainMovie() }
        val isAsc = true
        val successResponse = GeneralResponse(true, "", mockedMovies)
        val expectedResult = Resource.success(movies)

        //Prepare DB
        coEvery { roomDataSource.isMoviesNotEmpty(isAsc) }.returns(false)
        coEvery { kinemaDataSource.searchMovies(filterAttribute) }.returns(successResponse)
        coJustRun { roomDataSource.saveSearchMovieParameters(filterAttribute) }
        coJustRun { roomDataSource.saveFilteredAttributes(filterAttribute) }
        coEvery { roomDataSource.getCurrentLocation() }.returns(Coordinate(0.0, 0.0))
        coJustRun { roomDataSource.saveMovies(movies) }
        coEvery { roomDataSource.getMovies(isAsc) }.returns(movies)

        //----Act
        val result = runBlocking { repository.loadMovies(filterAttribute, isAsc) }
        //I want to make sure mocked.x wasn't called anymore
        coVerify(exactly = 1) {
            roomDataSource.getMovies(isAsc)
            kinemaDataSource.searchMovies(filterAttribute)
            roomDataSource.saveMovies(movies)
            roomDataSource.saveSearchMovieParameters(filterAttribute)
            roomDataSource.saveFilteredAttributes(filterAttribute)
        }

        coVerifySequence {
            roomDataSource.isMoviesNotEmpty(isAsc)
            kinemaDataSource.searchMovies(filterAttribute)
            roomDataSource.saveSearchMovieParameters(filterAttribute)
            roomDataSource.saveFilteredAttributes(filterAttribute)
            roomDataSource.saveMovies(movies)
            roomDataSource.getMovies(isAsc)
        }

        assertEquals(expectedResult, result)
    }

    @Test
    fun `load attributes from server success`() {
        //----Arrange
        val filterAttribute = mockedFilterAttribute.toDomainFilterAttribute()
        val attribute = mockedAttributes
        val successResponse = GeneralResponse(true, "", attribute)
        val expectedResult = Resource.success(attribute.toDomainAttribute())
        //Prepare DB
        coEvery { kinemaDataSource.getAttributes(filterAttribute) }.returns(successResponse)
        coEvery { roomDataSource.getAttributes() }.returns(attribute.toDomainAttribute())
        coJustRun { roomDataSource.saveAttributes(attribute) }

        //----Act
        val result = runBlocking { repository.loadAttributes(filterAttribute) }
        //I want to make sure mocked.x wasn't called anymore
        coVerify(exactly = 1) {
            roomDataSource.getAttributes()
            roomDataSource.saveAttributes(attribute)
            kinemaDataSource.getAttributes(filterAttribute)
        }

        assertEquals(expectedResult, result)
    }

    @Test
    fun `load attributes from server error`() {
        //----Arrange
        val filterAttribute = mockedFilterAttribute.toDomainFilterAttribute()
        val attribute = mockedAttributes
        val successResponse = GeneralResponse(false, "", attribute)
        val expectedResult = Resource.error("", null)
        //Prepare DB
        coEvery { kinemaDataSource.getAttributes(filterAttribute) }.returns(successResponse)

        //----Act
        val result = runBlocking { repository.loadAttributes(filterAttribute) }
        //I want to make sure mocked.x wasn't called anymore
        coVerify(exactly = 1) {
            kinemaDataSource.getAttributes(filterAttribute)
        }

        assertEquals(expectedResult, result)
    }

    @Test
    fun `get filtered attributes`() {
        //Prepare DB
        coEvery { roomDataSource.getFilteredAttributes() }.returns(mockedFilterAttribute.toDomainFilterAttribute())

        repository.getFilteredAttributes()
        coVerify(exactly = 1) { roomDataSource.getFilteredAttributes() }
    }

    @Test
    fun `update filtered attributes`() {
        val data = mockedFilterAttribute.toDomainFilterAttribute()
        //Prepare DB
        coJustRun { roomDataSource.saveFilteredAttributes(data) }

        repository.updateFilteredAttributes(data)

        coVerify(exactly = 1) { roomDataSource.saveFilteredAttributes(data) }
    }

    @Test
    fun `get watchlist movies`() {
        val data = listOf(mockedWatchlist)
        val isAsc = true
        //Prepare DB
        coEvery { roomDataSource.getWatchlistMovies(isAsc) }.returns(data)

        runBlocking { repository.getWatchlistMovies(isAsc) }

        coVerify(exactly = 1) { roomDataSource.getWatchlistMovies(isAsc) }
    }

    @Test
    fun `get watchlist movie`() {
        val data = mockedWatchlist
        //Prepare DB
        coEvery { roomDataSource.getWatchlistMovie(data) }.returns(data)

        runBlocking { repository.getWatchlistMovie(data) }

        coVerify(exactly = 1) { roomDataSource.getWatchlistMovie(data) }
    }

    @Test
    fun `add watchlist movie`() {
        val data = mockedWatchlist
        //Prepare DB
        coJustRun { roomDataSource.addWatchlistMovie(data) }

        runBlocking { repository.addWatchlistMovie(data) }

        coVerify(exactly = 1) { roomDataSource.addWatchlistMovie(data) }
    }

    @Test
    fun `delete watchlist movie`() {
        val data = mockedWatchlist
        //Prepare DB
        coJustRun { roomDataSource.deleteWatchlistMovie(data) }

        runBlocking { repository.deleteWatchlistMovie(data) }

        coVerify(exactly = 1) { roomDataSource.deleteWatchlistMovie(data) }
    }

    @Test
    fun `get current location`() {
        val data = mockedCurrentLocation

        //Prepare DB
        coEvery { roomDataSource.getCurrentLocation() }.returns(data)

        repository.getCurrentLocation()

        coVerify(exactly = 1) { roomDataSource.getCurrentLocation() }
    }

    @Test
    fun `update current location`() {
        val data = mockedCurrentLocation
        //Prepare DB
        coJustRun { roomDataSource.saveCurrentLocation(data) }

        repository.updateCurrentLocation(data)

        coVerify(exactly = 1) { roomDataSource.saveCurrentLocation(data) }
    }

    @Test
    fun `get sort WatchMovieList Order`() {
        val isAsc = true

        //Prepare DB
        coEvery { roomDataSource.getSortWatchMovieList() }.returns(isAsc)

        repository.getSortWatchMovieListOrder()

        coVerify(exactly = 1) { roomDataSource.getSortWatchMovieList() }
    }

    @Test
    fun `update sort WatchMovieList Order`() {
        val isAsc = true
        //Prepare DB
        coJustRun { roomDataSource.saveSortWatchMovieList(isAsc) }

        repository.updateWatchMovieListOrder(isAsc)

        coVerify(exactly = 1) { roomDataSource.saveSortWatchMovieList(isAsc) }
    }

    @Test
    fun `get sort MovieList Order`() {
        val isAsc = true

        //Prepare DB
        coEvery { roomDataSource.getSortMovieList() }.returns(isAsc)

        repository.getSortMovieListOrder()

        coVerify(exactly = 1) { roomDataSource.getSortMovieList() }
    }

    @Test
    fun `update sort MovieList Order`() {
        val isAsc = true
        //Prepare DB
        coJustRun { roomDataSource.saveSortMovieList(isAsc) }

        repository.updateMovieListOrder(isAsc)

        coVerify(exactly = 1) { roomDataSource.saveSortMovieList(isAsc) }
    }
}