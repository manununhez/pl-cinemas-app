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
import today.kinema.vo.WatchlistMovie

class MovieViewModel @ViewModelInject constructor(
    private val repository: KinemaRepository
) : ViewModel() {

    private val _currentFilterAttribute = MutableLiveData<FilterAttribute>()
    private val _movies = MutableLiveData<Resource<List<Movie>>>()
    private val _attributes = MutableLiveData<Resource<Attribute>>()
    private val _watchlist = MutableLiveData<List<WatchlistMovie>>()

    val watchlist: LiveData<List<WatchlistMovie>>
        get() = _watchlist

    val movies: LiveData<Resource<List<Movie>>>
        get() = _movies

    val currentFilterAttribute: LiveData<FilterAttribute>
        get() = _currentFilterAttribute

    val attributes: LiveData<Resource<Attribute>>
        get() = _attributes

    init {
        _currentFilterAttribute.value = repository.getFilteredAttributes()
        initWatchlist()
        loadAttributes()
        loadMovies()
    }

    private fun loadAttributes() {
        viewModelScope.launch {
            _attributes.value = Resource.loading(null)
            _attributes.value =
                repository.loadAttributes(
                    _currentFilterAttribute.value!!
                )
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _movies.value = Resource.loading(null)
            _movies.value =
                repository.loadMovies(
                    _currentFilterAttribute.value!!,
                    repository.getSortMovieListOrder()
                )
        }
    }

    private fun saveFilteredAttributes(filterAttribute: FilterAttribute) {
        _currentFilterAttribute.value = filterAttribute

        repository.updateFilteredAttributes(_currentFilterAttribute.value!!)
    }

    fun updateMovies() {
        val selectedAttributes = repository.getFilteredAttributes()
        val filterAttribute = _currentFilterAttribute.value!!

        if (filterAttribute != selectedAttributes) {
            saveFilteredAttributes(selectedAttributes)
            loadMovies()
            loadAttributes()
        }
    }

    fun setDateMoviesTitle(newDate: String) {
        val filterAttribute = _currentFilterAttribute.value!!
        val updatedFilterAttribute = FilterAttribute(
            filterAttribute.city,
            newDate,
            filterAttribute.cinema,
            filterAttribute.language
        )

        saveFilteredAttributes(updatedFilterAttribute)
        loadMovies()
    }

    fun updateMovieListOrder() {
        val isAsc = repository.getSortMovieListOrder()
        repository.updateMovieListOrder(!isAsc)
        loadMovies()
    }

    fun retry() {
        loadMovies()
    }

    fun initWatchlist() {
        viewModelScope.launch {
            _watchlist.value =
                repository.getWatchlistMovies(repository.getSortWatchMovieListOrder())
        }
    }

    fun updateWatchlist() {
        viewModelScope.launch {
            val list = repository.getWatchlistMovies(repository.getSortWatchMovieListOrder())
            if (_watchlist.value != list)
                _watchlist.value = list
        }
    }
}