package today.kinema.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import today.kinema.TestUtil.mockedMovie
import today.kinema.data.db.KinemaDb
import today.kinema.data.db.MovieDao
import today.kinema.data.db.WatchlistMovieDao
import today.kinema.data.db.model.WatchlistMovie
import today.kinema.data.toRoomMovie
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class KinemaDBTest {
    private lateinit var movieDao: MovieDao
    private lateinit var watchlistMovieDao: WatchlistMovieDao
    private lateinit var db: KinemaDb

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, KinemaDb::class.java
        ).build()
        movieDao = db.movieDao()
        watchlistMovieDao = db.watchlistMovieDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeMoviesAndReadInListAscDesc() = runBlocking {
        val mockedMovie = mockedMovie.toRoomMovie()
        val movies = listOf(
            mockedMovie.copy(id = "1", title = "Shrek 1"),
            mockedMovie.copy(id = "2", title = "Aquaman"),
            mockedMovie.copy(id = "3", title = "Superman vs Batman")
        )
        val moviesResultAsc = movies.sortedBy { it.title }
        val moviesResultDesc = moviesResultAsc.sortedByDescending { it.title }

        movieDao.insertMovies(movies)

        val result = movieDao.getMovies(true)
        assertEquals(moviesResultAsc, result)

        val resultDesc = movieDao.getMovies(false)
        assertEquals(moviesResultDesc, resultDesc)
    }

    @Test
    @Throws(Exception::class)
    fun writeMoviesAndReadFirstElement() = runBlocking {
        val mockedMovie = mockedMovie.toRoomMovie()
        val movies = listOf(
            mockedMovie.copy(id = "1", title = "Shrek 1"),
            mockedMovie.copy(id = "2", title = "Aquaman"),
            mockedMovie.copy(id = "3", title = "Superman vs Batman")
        )
        val moviesResult = movies[0] //first element inserted

        movieDao.insertMovies(movies)

        val result = movieDao.getFirstMovie()
        assertEquals(moviesResult, result)
    }

    @Test
    @Throws(Exception::class)
    fun writeMoviesAndClearAndReadEmptyList() = runBlocking {
        val mockedMovie = mockedMovie.toRoomMovie()
        val movies = listOf(
            mockedMovie.copy(id = "1", title = "Shrek 1"),
            mockedMovie.copy(id = "2", title = "Aquaman"),
            mockedMovie.copy(id = "3", title = "Superman vs Batman")
        )

        movieDao.insertMovies(movies)

        val resultList = movieDao.getMovies(true)
        assertThat(resultList, not(emptyList()))

        movieDao.clear()

        val result = movieDao.getMovies(true)
        assertThat(result, `is`(emptyList()))
    }

    @Test
    @Throws(Exception::class)
    fun writeWatchlistMoviesAndReadInListAscDesc() = runBlocking {
        val mockedMovie = mockedMovie.toRoomMovie()
        val watchlist = listOf(
            WatchlistMovie(mockedMovie.copy(id = "1", title = "Shrek 1", dateTitle = "2020-04-05")),
            WatchlistMovie(mockedMovie.copy(id = "2", title = "AntMan", dateTitle = "2020-01-15")),
            WatchlistMovie(mockedMovie.copy(id = "3", title = "Batman vs Superman", dateTitle = "2020-10-30")))

        val watchlistResultAsc = watchlist.sortedBy { it.dateTitle }
        val watchlistResultDesc = watchlist.sortedByDescending { it.dateTitle }

        watchlistMovieDao.insert(watchlist[0])
        watchlistMovieDao.insert(watchlist[1])
        watchlistMovieDao.insert(watchlist[2])

        val result = watchlistMovieDao.getWatchlistMovies(true)
        assertEquals(watchlistResultAsc, result)

        val resultDesc = watchlistMovieDao.getWatchlistMovies(false)
        assertEquals(watchlistResultDesc, resultDesc)
    }

    @Test
    @Throws(Exception::class)
    fun writeWatchlistMoviesAndDeleteItem() = runBlocking {
        val mockedMovie = mockedMovie.toRoomMovie()
        val watchlist = listOf(
            WatchlistMovie(mockedMovie.copy(id = "1", title = "Shrek 1", dateTitle = "2020-04-05")),
            WatchlistMovie(mockedMovie.copy(id = "2", title = "AntMan", dateTitle = "2020-01-15")),
            WatchlistMovie(mockedMovie.copy(id = "3", title = "Batman vs Superman", dateTitle = "2020-10-30")))

        val watchlist2 = listOf(
            WatchlistMovie(mockedMovie.copy(id = "1", title = "Shrek 1", dateTitle = "2020-04-05")),
            WatchlistMovie(mockedMovie.copy(id = "3", title = "Batman vs Superman", dateTitle = "2020-10-30")))

        val watchlistResultAsc = watchlist.sortedBy { it.dateTitle }
        val watchlistResultAsc2 = watchlist2.sortedBy { it.dateTitle }

        watchlistMovieDao.insert(watchlist[0])
        watchlistMovieDao.insert(watchlist[1])
        watchlistMovieDao.insert(watchlist[2])

        val result = watchlistMovieDao.getWatchlistMovies(true)
        assertEquals(watchlistResultAsc, result)

        watchlistMovieDao.delete(watchlist[1])

        val result2 = watchlistMovieDao.getWatchlistMovies(true)
        assertEquals(watchlistResultAsc2, result2)

    }

    @Test
    @Throws(Exception::class)
    fun writeWatchlistMoviesAndCheckItem() = runBlocking {
        val mockedMovie = mockedMovie.toRoomMovie()
        val watchlist = listOf(
            WatchlistMovie(mockedMovie.copy(id = "1", title = "Shrek 1", dateTitle = "2020-04-05")),
            WatchlistMovie(mockedMovie.copy(id = "2", title = "AntMan", dateTitle = "2020-01-15")),
            WatchlistMovie(mockedMovie.copy(id = "3", title = "Batman vs Superman", dateTitle = "2020-10-30")))
        val watchlistResultAsc = watchlist.sortedBy { it.dateTitle }

        watchlistMovieDao.insert(watchlist[0])
        watchlistMovieDao.insert(watchlist[1])
        watchlistMovieDao.insert(watchlist[2])

        val result = watchlistMovieDao.getWatchlistMovies(true)
        assertEquals(watchlistResultAsc, result)

        val elementDoesNotExists = watchlistMovieDao.checkIfWatchMovieExists(4, "2020-09-06")
        assertThat(elementDoesNotExists, `is`(0))

        val elementExists = watchlistMovieDao.checkIfWatchMovieExists(watchlist[1].id, watchlist[1].dateTitle)
        assertThat(elementExists, not(0))

    }
}