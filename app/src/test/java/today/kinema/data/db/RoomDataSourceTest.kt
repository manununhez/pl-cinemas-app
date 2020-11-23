package today.kinema.data.db

import android.content.SharedPreferences
import com.google.gson.Gson
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
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var watchlistDao: WatchlistMovieDao

    @Mock
    private lateinit var db: KinemaDb

    private lateinit var roomDataSource: RoomDataSource

    @Before
    fun setup() {
        roomDataSource = RoomDataSource(sharedPreferences, gson, db)
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