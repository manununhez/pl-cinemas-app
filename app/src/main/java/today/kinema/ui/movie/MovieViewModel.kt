package today.kinema.ui.movie

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import today.kinema.data.api.Resource
import today.kinema.repository.KinemaRepository
import today.kinema.vo.Attribute
import today.kinema.vo.FilterAttribute
import today.kinema.vo.Movie

class MovieViewModel @ViewModelInject constructor(
    private val repository: KinemaRepository
) : ViewModel() {

    private val _currentFilterAttribute = MutableLiveData<FilterAttribute>()
    private val _movies = MutableLiveData<Resource<List<Movie>>>()
    private val _attributes = MutableLiveData<Resource<Attribute>>()

    val movies: LiveData<Resource<List<Movie>>>
        get() = _movies

    val currentFilterAttribute: LiveData<FilterAttribute>
        get() =_currentFilterAttribute

    val attributes: LiveData<Resource<Attribute>>
        get() = _attributes


    init {
        _currentFilterAttribute.value = repository.getFilteredAttributes()
        initAttributes()
    }


    private fun initAttributes() {
        viewModelScope.launch {
            _attributes.value = Resource.loading(null)
            _attributes.value =
                repository.loadAttributes()
        }
    }

    private fun initMovies() {
        viewModelScope.launch {
            _movies.value = Resource.loading(null)
            _movies.value =
                repository.loadMovies(
                    _currentFilterAttribute.value!!,
                    repository.getSortMovieList()
                )
        }
    }

    private fun saveFilteredAttributes() {
        repository.saveFilteredAttributes(_currentFilterAttribute.value!!)
    }

    fun loadMovies() {
        val selectedAttributes = repository.getFilteredAttributes()
        val filterAttribute = _currentFilterAttribute.value!!

        if (filterAttribute != selectedAttributes) {
            _currentFilterAttribute.value = selectedAttributes
        }

        saveFilteredAttributes()
        initMovies()
    }


    fun setDateMoviesTitle(newDate: String) {
        val filterAttribute = _currentFilterAttribute.value!!
        filterAttribute.date = newDate

        _currentFilterAttribute.value = filterAttribute
        saveFilteredAttributes()
        initMovies()
    }

    fun updateMovieListOrder() {
        val isAsc = repository.getSortMovieList()
        repository.updateMovieListOrder(!isAsc)
        initMovies()
    }

    fun retry() {
        initMovies()
    }
}