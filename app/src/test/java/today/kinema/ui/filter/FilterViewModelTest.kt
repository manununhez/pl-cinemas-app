package today.kinema.ui.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import today.kinema.data.toDomainFilterAttribute
import today.kinema.repository.KinemaRepository
import today.kinema.util.TestUtil.mockedFilterAttribute
import today.kinema.vo.FilterAttribute

@RunWith(MockitoJUnitRunner::class)
class FilterViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: KinemaRepository

    @Mock
    private lateinit var filterAttrObserver: Observer<FilterAttribute>

    private lateinit var viewModel: FilterViewModel

    @Before
    fun setUp() {
        `when`(repository.getFilteredAttributes()).thenReturn(mockedFilterAttribute.toDomainFilterAttribute())
        viewModel = FilterViewModel(repository)
        //init{}
        viewModel.currentFilterAttribute.observeForever(filterAttrObserver)

    }

    @Test
    fun `a new cinema is selected for filter movies and it is added to filter attributes and saved locally`() {
        val cinemaSelected = "Cinema City"
        val clearSelection = false
        val filterAttribute = mockedFilterAttribute.toDomainFilterAttribute()

        val cinemas = mockedFilterAttribute.cinema.toMutableList()
        cinemas.add(cinemaSelected)
        val updatedFilterAttribute = FilterAttribute(
            filterAttribute.city,
            filterAttribute.date,
            cinemas,
            filterAttribute.language
        )

        viewModel.setMoviesCinemas(cinemaSelected, clearSelection)

        verify(filterAttrObserver).onChanged(updatedFilterAttribute)

        verify(repository).updateFilteredAttributes(updatedFilterAttribute)
    }

    @Test
    fun `'select all' cinemas is selected for filter movies and cinemas list in filter attributes is empty and saved locally`() {
        val clearSelection = true
        val filterAttribute = mockedFilterAttribute.toDomainFilterAttribute()

        val updatedFilterAttribute = FilterAttribute(
            filterAttribute.city,
            filterAttribute.date,
            listOf(),
            filterAttribute.language
        )

        viewModel.setMoviesCinemas("", clearSelection)

        verify(filterAttrObserver).onChanged(updatedFilterAttribute)

        verify(repository).updateFilteredAttributes(updatedFilterAttribute)
    }

    @Test
    fun `a new language is selected for filter movies and it is added to filter attributes and saved locally`() {
        val languageSelected = "franzuski"
        val clearSelection = false
        val filterAttribute = mockedFilterAttribute.toDomainFilterAttribute()
        val languages = mockedFilterAttribute.language.toMutableList()
        languages.add(languageSelected)

        val updatedFilterAttribute = FilterAttribute(
            filterAttribute.city,
            filterAttribute.date,
            filterAttribute.cinema,
            languages
        )

        viewModel.setMoviesLanguage(languageSelected, clearSelection)

        verify(filterAttrObserver).onChanged(updatedFilterAttribute)

        verify(repository).updateFilteredAttributes(updatedFilterAttribute)
    }

    @Test
    fun `'select all' languages is selected for filter movies and languages list in filter attributes is empty and saved locally`() {
        val clearSelection = true
        val filterAttribute = mockedFilterAttribute.toDomainFilterAttribute()

        val updatedFilterAttribute = FilterAttribute(
            filterAttribute.city,
            filterAttribute.date,
            filterAttribute.cinema,
            listOf()
        )

        viewModel.setMoviesLanguage("", clearSelection)

        verify(filterAttrObserver).onChanged(updatedFilterAttribute)

        verify(repository).updateFilteredAttributes(updatedFilterAttribute)
    }

    @Test
    fun `a new city is selected for filter movies and it is added to filter attributes and saved locally`() {
        val citySelected = "Krakow"
        val filterAttribute = mockedFilterAttribute.toDomainFilterAttribute()

        val updatedFilterAttribute = FilterAttribute(
            citySelected,
            filterAttribute.date,
            filterAttribute.cinema,
            filterAttribute.language
        )

        viewModel.setMoviesCity(citySelected)

        verify(filterAttrObserver).onChanged(updatedFilterAttribute)

        verify(repository).updateFilteredAttributes(updatedFilterAttribute)
    }

    @Test
    fun updateElementInList() {
        val list = listOf("1", "2", "3", "4", "5")
        val updatedListAfterAddedNewElement = listOf("1", "2", "3", "4", "5", "12")
        val updatedListAfterRemoveElement = listOf("1", "3", "4", "5")

        assertThat(viewModel.updateElementInList(list, "12", false), `is`(updatedListAfterAddedNewElement))
        assertThat(viewModel.updateElementInList(list, "2", false), `is`(updatedListAfterRemoveElement))
        assertThat(viewModel.updateElementInList(list, "", true), `is`(listOf()))
    }

}