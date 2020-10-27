package today.kinema.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import today.kinema.db.WatchlistMovie
import today.kinema.repository.MovieRepository
import today.kinema.util.AbsentLiveData
import today.kinema.vo.Coordinate
import today.kinema.vo.FilterAttribute
import today.kinema.vo.Movies
import today.kinema.vo.Resource

class SharedMovieViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchMovieQuery = MutableLiveData<CinemaMoviesId>()
    private val _sortWatchList = MutableLiveData<Boolean>()
    private val _currentFilterAttribute = MutableLiveData<FilterAttribute>()

    val movies: LiveData<Resource<List<Movies>>> =
        Transformations.switchMap(_searchMovieQuery) { input ->
            input.ifExists { fAttr ->
                repository.loadMovies(fAttr)
            }
        }

    val currentFilterAttribute: LiveData<FilterAttribute>
        get() = _currentFilterAttribute

    val watchlist: LiveData<List<WatchlistMovie>> =
        Transformations.switchMap(_sortWatchList) {
            repository.getWatchlistMovies(it)
        }

    val attributes = repository.loadAttributes()


    init {
        _sortWatchList.value = repository.getSortWatchList()
        _currentFilterAttribute.value = repository.getFilteredAttributes()
        _searchMovieQuery.value = CinemaMoviesId(repository.getFilteredAttributes())
    }


    fun loadMovies() {
        val selectedAttributes = repository.getFilteredAttributes()
        val filterAttribute = _currentFilterAttribute.value!!

        if (filterAttribute != selectedAttributes)
            _searchMovieQuery.value = CinemaMoviesId(filterAttribute)
    }

    fun setMoviesLanguage(filteredLanguage: String, clearSelection: Boolean) {
        val filterAttribute = _currentFilterAttribute.value!!

        if (clearSelection) {
            _currentFilterAttribute.value = FilterAttribute(
                filterAttribute.city,
                filterAttribute.date,
                filterAttribute.cinema,
                listOf()//empty list
            )
        } else {

            val languagesList = filterAttribute.language.toMutableList()
            if (languagesList.contains(filteredLanguage)) //checkbox
                languagesList.remove(filteredLanguage) //remove if exists
            else
                languagesList.add(filteredLanguage)//append new cinema to the list


            _currentFilterAttribute.value = FilterAttribute(
                filterAttribute.city,
                filterAttribute.date,
                filterAttribute.cinema,
                languagesList
            )
        }
    }

    fun setMoviesCinemas(filteredCinemas: String, clearSelection: Boolean) {
        val filterAttribute = _currentFilterAttribute.value!!

        if (clearSelection) {
            _currentFilterAttribute.value = FilterAttribute(
                filterAttribute.city,
                filterAttribute.date,
                listOf(), //empty list
                filterAttribute.language
            )
        } else {
            val cinemasList = filterAttribute.cinema.toMutableList()
            if (cinemasList.contains(filteredCinemas)) //checkbox
                cinemasList.remove(filteredCinemas) //remove if exists
            else
                cinemasList.add(filteredCinemas)//append new cinema to the list


            _currentFilterAttribute.value = FilterAttribute(
                filterAttribute.city,
                filterAttribute.date,
                cinemasList,
                filterAttribute.language
            )
        }
    }

    fun setMoviesCity(filteredCityName: String) {
        val filterAttribute = _currentFilterAttribute.value!!
        _currentFilterAttribute.value = FilterAttribute(
            filteredCityName,
            filterAttribute.date,
            filterAttribute.cinema,
            filterAttribute.language
        )
    }

    fun setDateMoviesTitle(newDate: String) {
        val filterAttribute = _currentFilterAttribute.value!!
        _currentFilterAttribute.value = FilterAttribute(
            filterAttribute.city,
            newDate,
            filterAttribute.cinema,
            filterAttribute.language
        )
    }

    fun setCurrentLocation(currentLocation: Coordinate) {
        repository.setCurrentLocation(currentLocation)
    }

    fun getWatchlistMovie(watchlistMovie: WatchlistMovie) =
        repository.getWatchlistMovie(watchlistMovie)

    fun addWatchlistMovie(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch {
            repository.addWatchlistMovie(watchlistMovie)
        }
    }

    fun removeWatchlistMovie(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch {
            repository.deleteWatchlistMovie(watchlistMovie)
        }
    }

    fun updateWatchListOrder() {
        val isAsc: Boolean = !_sortWatchList.value!!
        _sortWatchList.value = isAsc
        repository.setWatchListOrder(isAsc)
    }

    fun retry() {
        val filterAttribute = _currentFilterAttribute.value!!

        _searchMovieQuery.value = CinemaMoviesId(filterAttribute)
    }

    data class CinemaMoviesId(
        val filterAttribute: FilterAttribute
    ) {
        fun <T> ifExists(f: (FilterAttribute) -> LiveData<T>): LiveData<T> {
            return if (filterAttribute.city.isBlank() || filterAttribute.date.isBlank()) {
                AbsentLiveData.create()
            } else {
                f(filterAttribute)
            }
        }
    }
}