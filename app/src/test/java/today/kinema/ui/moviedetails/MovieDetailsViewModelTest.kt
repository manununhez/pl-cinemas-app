package today.kinema.ui.moviedetails

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
import today.kinema.data.toDomainCinema
import today.kinema.repository.KinemaRepository
import today.kinema.util.TestUtil.mockedCinema
import today.kinema.util.TestUtil.mockedCurrentLocation
import today.kinema.util.TestUtil.mockedWatchlistMovie

@ExperimentalCoroutinesApi
class MovieDetailsViewModelTest {

    /**
     * A JUnit rule that configures LiveData to execute each task synchronously
     */
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private var repository = mockk<KinemaRepository>()

    private lateinit var viewModel: MovieDetailsViewModel

    @Before
    fun setUp() {
        viewModel = MovieDetailsViewModel(testDispatcher, testDispatcher, testDispatcher, repository)
    }


    @Test
    fun `onRemoveWatchlistBtnClicked_remove item from watchlist and refresh watchlist`() {
        val itemWatchlist = mockedWatchlistMovie

        coJustRun { repository.deleteWatchlistMovie(itemWatchlist) }
        coEvery { repository.checkIfWatchMovieExists(itemWatchlist) }.returns(false)

        runBlocking { viewModel.onRemoveWatchlistBtnClicked(itemWatchlist) }

        coVerify(exactly = 1) {
            repository.checkIfWatchMovieExists(any())
            repository.deleteWatchlistMovie(any())
        }

        assertEquals(false, viewModel.watchlist.value)
    }
    @Test
    fun `onAddWatchlistBtnClicked_add item to watchlist and refresh watchlist`() {
        val itemWatchlist = mockedWatchlistMovie

        coJustRun { repository.addWatchlistMovie(itemWatchlist) }
        coEvery { repository.checkIfWatchMovieExists(itemWatchlist) }.returns(true)

        runBlocking { viewModel.onAddWatchlistBtnClicked(itemWatchlist) }

        coVerify(exactly = 1) {
            repository.checkIfWatchMovieExists(any())
            repository.addWatchlistMovie(any())
        }

        assertEquals(true, viewModel.watchlist.value)
    }

    @Test
    fun `order Cinemas By Distance`(){
        val cinemalist = listOf(
            mockedCinema.toDomainCinema().copy(distance = 15.0F),
            mockedCinema.toDomainCinema().copy(distance = 125.0F),
            mockedCinema.toDomainCinema().copy(distance = 1.0F),
            mockedCinema.toDomainCinema().copy(distance = 55.0F))

        val cinemalistOrdered = listOf(
            mockedCinema.toDomainCinema().copy(distance = 1.0F),
            mockedCinema.toDomainCinema().copy(distance = 15.0F),
            mockedCinema.toDomainCinema().copy(distance = 55.0F),
            mockedCinema.toDomainCinema().copy(distance = 125.0F))


        coEvery { repository.getCurrentLocation() }.returns(mockedCurrentLocation)

        runBlocking { viewModel.orderCinemasByDistance(cinemalist) }
        assertEquals(cinemalistOrdered, viewModel.cinemas.value)
    }
}