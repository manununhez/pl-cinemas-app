package today.kinema.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import today.kinema.repository.KinemaRepository
import today.kinema.util.TestUtil.mockedCurrentLocation

@RunWith(MockitoJUnitRunner::class)
class SharedMovieViewModelTest {
    /**
     * InstantTaskExecutorRule is a rule that swaps out
     * that executor and replaces it with synchronous one. This will make sure that, when
     * you're using LiveData with the ViewModel, it's all run synchronously in the tests.
     * this rule basically does is allow us to run LiveData synchronously.
     * This rule is from the core-testing package that was imported earlier.
     */
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: KinemaRepository

    private lateinit var viewModel: SharedMovieViewModel

    @Before
    fun setUp() {
        viewModel = SharedMovieViewModel(repository)
    }

    @Test
    fun `setCurrentLocation is called`() {
       viewModel.setCurrentLocation(mockedCurrentLocation)
    }
}