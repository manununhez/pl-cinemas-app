package today.kinema.ui.watchlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import today.kinema.repository.KinemaRepository
import today.kinema.util.TestUtil.mockedWatchlistMovie
import today.kinema.util.TestUtil.mockedWatchlistMovieList

@ExperimentalCoroutinesApi
class WatchlistViewModelTest {

    /**
     * A JUnit rule that configures LiveData to execute each task synchronously
     */
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private var repository = mockk<KinemaRepository>()

    private lateinit var viewModel: WatchlistViewModel

    @Before
    fun setUp() {
        coEvery {repository.getSortWatchMovieListOrder() }.returns(true)
        viewModel = WatchlistViewModel(testDispatcher, testDispatcher, testDispatcher, repository)
    }

    @Test
    fun `refresh watchlist and observing results in livedata`() {
        coEvery { repository.getWatchlistMovies(any()) }.returns(mockedWatchlistMovieList)

        runBlocking { viewModel.refreshWatchlist() }

        coVerify(exactly = 1) { repository.getWatchlistMovies(any()) }

        assertEquals(mockedWatchlistMovieList, viewModel.watchlist.value)
    }

    @Test
    fun `onRemoveWatchlistBtnClicked_remove item from watchlist and refresh watchlist`() {
        val itemWatchlist = mockedWatchlistMovie
        val resultWatchlist = mockedWatchlistMovieList.toMutableList()
        resultWatchlist.remove(itemWatchlist)

        coJustRun { repository.deleteWatchlistMovie(itemWatchlist) }
        coEvery { repository.getWatchlistMovies(any()) }.returns(resultWatchlist)

        runBlocking { viewModel.onRemoveWatchlistBtnClicked(itemWatchlist) }

        coVerify(exactly = 1) {
            repository.getWatchlistMovies(any())
            repository.deleteWatchlistMovie(any())
        }

        assertEquals(resultWatchlist, viewModel.watchlist.value)
    }

    @Test
    fun `onSortMovieWatchlistBtnClicked_remove item from watchlist and refresh watchlist`() {
        val resultWatchlistDesc = listOf(
            mockedWatchlistMovie.copy(dateTitle = "2020-12-18"),
            mockedWatchlistMovie.copy(dateTitle = "2020-05-04"),
            mockedWatchlistMovie.copy(dateTitle = "2020-01-10")
        )

        val resultWatchlistAsc = listOf(
            mockedWatchlistMovie.copy(dateTitle = "2020-01-10"),
            mockedWatchlistMovie.copy(dateTitle = "2020-05-04"),
            mockedWatchlistMovie.copy(dateTitle = "2020-12-18")
        )

        coJustRun { repository.updateWatchMovieListOrder(any()) }
        coEvery { repository.getWatchlistMovies(true) }.returns(resultWatchlistAsc)
        coEvery { repository.getWatchlistMovies(false) }.returns(resultWatchlistDesc)


        // 1st time clicked
        coEvery {repository.getSortWatchMovieListOrder() }.returns(true)
        runBlocking { viewModel.onSortMovieWatchlistBtnClicked() }

        coVerify(exactly = 1) {
            repository.updateWatchMovieListOrder(any())
            repository.getWatchlistMovies(any())
        }

        assertEquals(resultWatchlistDesc, viewModel.watchlist.value)
        assertEquals(false, viewModel.sortOrderList.value)


        // 2nd time clicked
        coEvery {repository.getSortWatchMovieListOrder() }.returns(false)
        runBlocking { viewModel.onSortMovieWatchlistBtnClicked() }

        coVerify(exactly = 2) {
            repository.updateWatchMovieListOrder(any())
            repository.getWatchlistMovies(any())
        }

        assertEquals(resultWatchlistAsc, viewModel.watchlist.value)
        assertEquals(true, viewModel.sortOrderList.value)

    }
}