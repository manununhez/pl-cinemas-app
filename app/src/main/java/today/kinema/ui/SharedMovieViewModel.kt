package today.kinema.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
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

    val movies: LiveData<Resource<List<Movies>>> =
        Transformations.switchMap(_searchMovieQuery) { input ->
            input.ifExists { fAttr ->
                repository.loadMovies(fAttr)
            }
        }

    private val _currentFilterAttribute = MutableLiveData<FilterAttribute>()
    val currentFilterAttribute: LiveData<FilterAttribute>
        get() = _currentFilterAttribute

    private val _watchlist = MutableLiveData<List<Movies>>()
    val watchlist: LiveData<List<Movies>>
        get() = _watchlist

    val attributes = repository.loadAttributes()


    init {
        _watchlist.value = repository.getWatchlist()
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

    fun setWatchlist(movie: Movies) {
        val watchlist = _watchlist.value!!.toMutableList()

        if (watchlist.contains(movie))
            watchlist.remove(movie)
        else
            watchlist.add(movie)

        _watchlist.value = watchlist
        repository.setWatchlist(watchlist)
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