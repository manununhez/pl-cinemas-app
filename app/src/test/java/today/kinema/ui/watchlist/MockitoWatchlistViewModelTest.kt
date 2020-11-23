package today.kinema.ui.watchlist

/**
 * For some reason, Mocking with Mockito return null when Mock repository.
 * Instead we use Mockk.
 * Error test -> fun `initialize watchlist and observing results in livedata with Mockito`()
 */
//@ExperimentalCoroutinesApi
//@RunWith(MockitoJUnitRunner::class)
//class MockitoWatchlistViewModelTest {
//
//    /**
//     * A JUnit rule that configures LiveData to execute each task synchronously
//     */
//    @get:Rule
//    val taskExecutorRule = InstantTaskExecutorRule()
//
//    /**
//     *  is a custom rule in this codebase that configures Dispatchers.Main to
//     *  use a TestCoroutineDispatcher from kotlinx-coroutines-test. This allows
//     *  tests to advance a virtual-clock for testing, and allows code to use
//     *  Dispatchers.Main in unit tests.
//     */
//    private val coroutineScope = MainCoroutineScopeRule()
//    private val testDispatcher = coroutineScope.dispatcher
//
//    @Mock
//    private lateinit var repository: KinemaRepository
//
//    @Mock
//    private lateinit var watchlistObserver: Observer<List<WatchlistMovie>>
//
//    private lateinit var viewModel: WatchlistViewModel
//
//    @Before
//    fun setUp() {
//        viewModel = WatchlistViewModel(testDispatcher, testDispatcher, repository)
//        `when`(repository.getSortWatchMovieListOrder()).thenReturn(true)
//        viewModel.watchlist.observeForever(watchlistObserver)
//    }
//
//
//    @Test
//    fun `initialize watchlist and observing results in livedata with Mockito`() =
//        coroutineScope.runBlockingTest {
//
//           // `when`(repository.getWatchlistMovies(true)).thenReturn(mockedWatchlistMovieList)
//
//            viewModel.refreshWatchlist()
//            verify(watchlistObserver).onChanged(mockedWatchlistMovieList)
//
//        }
//
//}