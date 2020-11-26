package today.kinema.data.db

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import today.kinema.data.toRoomWatchlistMovie
import today.kinema.util.TestUtil.mockedWatchlistMovie

@RunWith(MockitoJUnitRunner::class)
class RoomDataSourceTest {

    @Mock
    private lateinit var sharedPreferencesDB: SharedPreferencesDB

    @Mock
    private lateinit var watchlistDao: WatchlistMovieDao

    @Mock
    private lateinit var watchlistMovieDao: WatchlistMovieDao

    @Mock
    private lateinit var movieDao: MovieDao

    private lateinit var localDataSourceImpl: LocalDataSourceImpl

    @Before
    fun setup() {
        localDataSourceImpl = LocalDataSourceImpl(sharedPreferencesDB, watchlistMovieDao, movieDao)
    }

    @Test
    fun getWatchlistMovies() = runBlocking {
        val movies = listOf(mockedWatchlistMovie).map { it.toRoomWatchlistMovie() }


        `when`(watchlistDao.getWatchlistMovies(true)).thenReturn(movies)

        val result =
            watchlistDao.getWatchlistMovies(true)

        assertEquals(movies, result)
    }

}