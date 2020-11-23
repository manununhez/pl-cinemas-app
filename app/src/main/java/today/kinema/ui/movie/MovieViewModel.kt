package today.kinema.ui.movie

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import today.kinema.data.api.Resource
import today.kinema.di.MainDispatcher
import today.kinema.repository.KinemaRepository
import today.kinema.vo.Attribute
import today.kinema.vo.FilterAttribute
import today.kinema.vo.Movie
import today.kinema.vo.WatchlistMovie

class MovieViewModel @ViewModelInject constructor(
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val repository: KinemaRepository
) : ViewModel() {
    private val _currentFilterAttribute = MutableLiveData<FilterAttribute>()
    private val _movies = MutableLiveData<Resource<List<Movie>>>()
    private val _attributes = MutableLiveData<Resource<Attribute>>()
    private val _watchlist = MutableLiveData<List<WatchlistMovie>>()
    private val _sortOrderList = MutableLiveData<Boolean>()

    val sortOrderList: LiveData<Boolean>
        get() = _sortOrderList

    val watchlist: LiveData<List<WatchlistMovie>>
        get() = _watchlist

    val movies: LiveData<Resource<List<Movie>>>
        get() = _movies

    val currentFilterAttribute: LiveData<FilterAttribute>
        get() = _currentFilterAttribute

    val attributes: LiveData<Resource<Attribute>>
        get() = _attributes

    init {
        _sortOrderList.value = repository.getSortMovieListOrder()
        _currentFilterAttribute.value = repository.getFilteredAttributes()
        initWatchlist()
        loadAttributes()
        refreshMovieList()
    }

    private fun loadAttributes() {
        viewModelScope.launch(mainDispatcher) {
            _attributes.value = Resource.loading(null)
            _attributes.value =
                repository.loadAttributes(
                    _currentFilterAttribute.value!!
                )
        }
    }

    private fun refreshMovieList() {
        viewModelScope.launch(mainDispatcher) {
            _movies.value = Resource.loading(null)
            _movies.value =
                repository.loadMovies(
                    _currentFilterAttribute.value!!,
                    _sortOrderList.value!!
                )
        }
    }

    private fun saveFilteredAttributes(filterAttribute: FilterAttribute) {
        _currentFilterAttribute.value = filterAttribute

        repository.updateFilteredAttributes(filterAttribute)
    }

    private fun initWatchlist() {
        viewModelScope.launch(mainDispatcher) {
            _watchlist.value =
                repository.getWatchlistMovies(repository.getSortWatchMovieListOrder())
        }
    }

    private fun setDateMoviesTitle(newDate: String) {
        val filterAttribute = _currentFilterAttribute.value!!

        saveFilteredAttributes(filterAttribute.copy(date = newDate))
    }

    private fun updateMovieListOrder() {
        val isAsc = !_sortOrderList.value!!
        _sortOrderList.value = isAsc
        repository.updateMovieListOrder(isAsc)
    }

    fun onDateMovieBtnClicked(newDate: String) {
        setDateMoviesTitle(newDate)
        refreshMovieList()
    }

    fun onSortMovielistBtnClicked() {
        updateMovieListOrder()
        refreshMovieList()
    }

    /**
     * Update movie list in case of filteredAttributes change in FilterFragment
     */
    fun updateMovies() {
        val selectedAttributes = repository.getFilteredAttributes()
        val filterAttribute = _currentFilterAttribute.value!!

        if (filterAttribute != selectedAttributes) {
            saveFilteredAttributes(selectedAttributes)
            refreshMovieList()
            loadAttributes()
        }
    }

    fun updateWatchlist() {
        viewModelScope.launch(mainDispatcher) {
            val list = repository.getWatchlistMovies(repository.getSortWatchMovieListOrder())
            if (_watchlist.value != list)
                _watchlist.value = list
        }
    }

    fun retry() {
        refreshMovieList()
    }
}