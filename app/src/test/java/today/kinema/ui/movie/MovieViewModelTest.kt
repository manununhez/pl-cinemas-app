package today.kinema.ui.movie

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
import today.kinema.data.api.Resource
import today.kinema.data.toDomainAttribute
import today.kinema.data.toDomainFilterAttribute
import today.kinema.data.toDomainMovie
import today.kinema.repository.KinemaRepository
import today.kinema.util.TestUtil.mockedAttributes
import today.kinema.util.TestUtil.mockedAttributes2
import today.kinema.util.TestUtil.mockedFilterAttribute
import today.kinema.util.TestUtil.mockedMovies
import today.kinema.util.TestUtil.mockedMovies2
import today.kinema.util.TestUtil.mockedWatchlistMovieList
import today.kinema.util.TestUtil.mockedWatchlistMovieList2

@ExperimentalCoroutinesApi
class MovieViewModelTest {
    /**
     * A JUnit rule that configures LiveData to execute each task synchronously
     */
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private var repository = mockk<KinemaRepository>()

    private lateinit var viewModel: MovieViewModel

    private var sortMovieListOrder = true

    @Before
    fun setUp() {
        coEvery { repository.getSortWatchMovieListOrder() }.returns(true)
        coEvery { repository.getSortMovieListOrder() }.returns(sortMovieListOrder)
        coEvery { repository.getFilteredAttributes() }.returns(mockedFilterAttribute.toDomainFilterAttribute())
        coEvery { repository.getWatchlistMovies(any()) }.returns(
            mockedWatchlistMovieList
        )
        coEvery {
            repository.loadAttributes(any())
        }.returns(
            Resource.success(mockedAttributes.toDomainAttribute())
        )

        coEvery {
            repository.loadMovies(mockedFilterAttribute.toDomainFilterAttribute(), true)
        }.returns(
            Resource.success(mockedMovies.map { it.toDomainMovie() })
        )

        viewModel = MovieViewModel(testDispatcher, testDispatcher, repository)
    }

    @Test
    fun initializeValues() {
        coVerify(exactly = 1) {
            repository.getSortWatchMovieListOrder()
            repository.getSortMovieListOrder()
            repository.getFilteredAttributes()
            repository.getWatchlistMovies(any())
            repository.loadAttributes(any())

            repository.loadMovies(mockedFilterAttribute.toDomainFilterAttribute(), true)

        }
    }

    @Test
    fun `on Date Movie Btn Clicked and movies list from date selected is fetched`() {
        val newDate = "2020-10-25"
        val mockedFilterAttrModified =
            mockedFilterAttribute.toDomainFilterAttribute().copy(date = newDate)
        val movieReturnSuccess = Resource.success(mockedMovies2.map { it.toDomainMovie() })


        coEvery { repository.loadMovies(mockedFilterAttrModified, sortMovieListOrder) }.returns(
            movieReturnSuccess
        )
        coJustRun { repository.updateFilteredAttributes(mockedFilterAttrModified) }

        runBlocking { viewModel.onDateMovieBtnClicked(newDate) }

        assertEquals(movieReturnSuccess, viewModel.movies.value)
        assertEquals(mockedFilterAttrModified, viewModel.currentFilterAttribute.value)
    }

    @Test
    fun `on Sort Movie list Btn Clicked and movies list is reorder by movie title asc-desc`() {
        val movieReturnSuccess = Resource.success(mockedMovies2.map { it.toDomainMovie() })
        val sortMovieOrderAfterClicked = !sortMovieListOrder

        coEvery {
            repository.loadMovies(
                mockedFilterAttribute.toDomainFilterAttribute(),
                sortMovieOrderAfterClicked
            )
        }.returns(movieReturnSuccess)
        coJustRun { repository.updateMovieListOrder(sortMovieOrderAfterClicked) }

        runBlocking { viewModel.onSortMovielistBtnClicked() }

        assertEquals(sortMovieOrderAfterClicked, viewModel.sortOrderList.value)
        assertEquals(
            mockedFilterAttribute.toDomainFilterAttribute(),
            viewModel.currentFilterAttribute.value
        )
    }

    @Test
    fun `movies list not updated 'cause filter attributes values are the same`() {
        val movieReturnSuccess = Resource.success(mockedMovies2.map { it.toDomainMovie() })


        coEvery { repository.getFilteredAttributes() }.returns(mockedFilterAttribute.toDomainFilterAttribute())
        coJustRun { repository.updateFilteredAttributes(mockedFilterAttribute.toDomainFilterAttribute()) }
        coEvery {
            repository.loadMovies(
                mockedFilterAttribute.toDomainFilterAttribute(),
                sortMovieListOrder
            )
        }.returns(movieReturnSuccess)

        runBlocking { viewModel.updateMovies() }

        //Only called once in setup
        coVerify(exactly = 1) {
            repository.loadMovies(any(), any())
            repository.loadAttributes(any())
        }

        coVerify(exactly = 0) {
            repository.updateFilteredAttributes(any())
        }
    }

    @Test
    fun `movies list updated 'cause filter attributes values are not the same`() {
        val newDate = "2020-10-25"
        val mockedFilterAttrModified =
            mockedFilterAttribute.toDomainFilterAttribute().copy(date = newDate)
        val movieReturnSuccess = Resource.success(mockedMovies2.map { it.toDomainMovie() })
        val attribModified = Resource.success(mockedAttributes2.toDomainAttribute())

        coEvery { repository.getFilteredAttributes() }.returns(mockedFilterAttrModified)
        coEvery { repository.loadMovies(mockedFilterAttrModified, sortMovieListOrder) }.returns(movieReturnSuccess)
        coEvery { repository.loadAttributes(any()) }.returns(attribModified)
        coJustRun {  repository.updateFilteredAttributes(mockedFilterAttrModified) }

        runBlocking {  viewModel.updateMovies() }

        //Only called once in setup
        coVerify(exactly = 2) {
            repository.loadMovies(any(), any())
            repository.loadAttributes(any())
        }

        coVerify(exactly = 1) {
            repository.updateFilteredAttributes(any())
        }
        assertEquals(mockedFilterAttrModified, viewModel.currentFilterAttribute.value)
        assertEquals(movieReturnSuccess, viewModel.movies.value)
        assertEquals(attribModified, viewModel.attributes.value)
    }

    @Test
    fun `watchlist not updated 'cause watchlist values are the same`() {
        coEvery { repository.getWatchlistMovies(any()) }.returns(
            mockedWatchlistMovieList
        )

        runBlocking { viewModel.updateWatchlist() }

        assertEquals(mockedWatchlistMovieList, viewModel.watchlist.value)
    }

    @Test
    fun `watchlist updated 'cause watchlist values are not the same`() {
        coEvery { repository.getWatchlistMovies(any()) }.returns(
            mockedWatchlistMovieList2
        )

        runBlocking { viewModel.updateWatchlist() }

        assertEquals(mockedWatchlistMovieList2, viewModel.watchlist.value)
    }

}