package today.kinema.data.db

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import today.kinema.data.toDomainFilterAttribute
import today.kinema.data.toDomainMovie
import today.kinema.data.toRoomFilterAttribute
import today.kinema.data.toRoomMovie
import today.kinema.util.DateUtils
import today.kinema.util.TestUtil.mockedFilterAttribute
import today.kinema.util.TestUtil.mockedMovies
import today.kinema.vo.Movie
import java.util.*
import today.kinema.data.db.model.Movie as RoomMovie


@RunWith(MockitoJUnitRunner::class)
class LocalDataSourceImplTest {

    private var sharedPreferencesDB = mockk<SharedPreferencesDB>()
    private var watchlistMovieDao = mockk<WatchlistMovieDao>()
    private var movieDao = mockk<MovieDao>()

    private lateinit var localDataSourceImpl: LocalDataSourceImpl

    @Before
    fun setup() {
        localDataSourceImpl = LocalDataSourceImpl(sharedPreferencesDB, watchlistMovieDao, movieDao)
    }


    @Test
    fun `get filter attributes return current date when saved date value is before today`() {
        val today = DateUtils.dateFormat(Date())
        val filterAttrBeforeToday = mockedFilterAttribute.copy(date = DateUtils.getDaysAgo(2))
        coEvery { sharedPreferencesDB.getFilteredAttributes() }.returns(filterAttrBeforeToday.toRoomFilterAttribute())

        val result = localDataSourceImpl.getFilteredAttributes()

        assertEquals(today, result.date)
    }

    @Test
    fun `get filter attributes return saved date value when date is not before today`() {
        val today = DateUtils.dateFormat(Date())
        val filterAttrToday = mockedFilterAttribute.copy(date = today)
        coEvery { sharedPreferencesDB.getFilteredAttributes() }.returns(filterAttrToday.toRoomFilterAttribute())

        val result = localDataSourceImpl.getFilteredAttributes()

        assertEquals(filterAttrToday.toDomainFilterAttribute(), result)
    }

    @Test
    fun `get movies return empty list when there are not saved movies`() {
        val isAsc = true
        val moviesReturn = listOf<RoomMovie>()
        val moviesExpectedReturn = listOf<Movie>()

        coEvery { sharedPreferencesDB.getFilteredAttributes() }.returns(mockedFilterAttribute.toRoomFilterAttribute())
        coEvery { sharedPreferencesDB.getSearchMovieParameters() }.returns(mockedFilterAttribute.toRoomFilterAttribute())
        coEvery { movieDao.getMovies(isAsc) }.returns(moviesReturn)

        val result = runBlocking { localDataSourceImpl.getMovies(isAsc) }

        assertEquals(moviesExpectedReturn, result)
    }

    @Test
    fun `get movies return empty list when current movies filter attributes are different from last movie search parameters`() {
        val isAsc = true
        val moviesReturn = mockedMovies.map { it.toRoomMovie() }
        val moviesExpectedReturn = listOf<Movie>()

        coEvery { sharedPreferencesDB.getFilteredAttributes() }.returns(mockedFilterAttribute.toRoomFilterAttribute())
        coEvery { sharedPreferencesDB.getSearchMovieParameters() }.returns(
            mockedFilterAttribute.copy(
                date = "2020-08-06"
            ).toRoomFilterAttribute()
        )
        coEvery { movieDao.getMovies(isAsc) }.returns(moviesReturn)

        val result = runBlocking { localDataSourceImpl.getMovies(isAsc) }

        assertEquals(moviesExpectedReturn, result)
    }

    @Test
    fun `get movies return empty list when saved movie date value is before today`() {
        val isAsc = true
        val moviesBeforeTodayReturn =
            mockedMovies.map { it.copy(dateTitle = DateUtils.getDaysAgo(2)).toRoomMovie() }
        val moviesExpectedReturn = listOf<Movie>()

        coEvery { sharedPreferencesDB.getFilteredAttributes() }.returns(mockedFilterAttribute.toRoomFilterAttribute())
        coEvery { sharedPreferencesDB.getSearchMovieParameters() }.returns(mockedFilterAttribute.toRoomFilterAttribute())
        coEvery { movieDao.getMovies(isAsc) }.returns(moviesBeforeTodayReturn)

        val result = runBlocking { localDataSourceImpl.getMovies(isAsc) }

        assertEquals(moviesExpectedReturn, result)
    }

    @Test
    fun `get movies return saved movies when saved movie date value is not before today`() {
        val isAsc = true
        val today = DateUtils.dateFormat(Date())
        val moviesTodayReturn = mockedMovies.map { it.copy(dateTitle = today).toRoomMovie() }
        val moviesExpectedReturn = mockedMovies.map { it.copy(dateTitle = today).toDomainMovie() }
        val filterAttrsToday = mockedFilterAttribute.copy(date = today).toRoomFilterAttribute()
        
        coEvery { sharedPreferencesDB.getFilteredAttributes() }.returns(filterAttrsToday)
        coEvery { sharedPreferencesDB.getSearchMovieParameters() }.returns(filterAttrsToday)
        coEvery { movieDao.getMovies(isAsc) }.returns(moviesTodayReturn)

        val result = runBlocking { localDataSourceImpl.getMovies(isAsc) }

        assertEquals(moviesExpectedReturn, result)
    }

}