package com.manudev.cinemaspl.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.manudev.cinemaspl.repository.MovieRepository
import com.manudev.cinemaspl.util.AbsentLiveData
import com.manudev.cinemaspl.vo.FilterAttribute
import com.manudev.cinemaspl.vo.Movies
import com.manudev.cinemaspl.vo.Resource

class SharedMovieViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _query = MutableLiveData<CinemaMoviesId>()

    val movies: LiveData<Resource<List<Movies>>> =
        Transformations.switchMap(_query) { input ->
            input.ifExists { fAttr ->
                repository.loadMovies(fAttr)
            }
        }

    private val _currentFilterAttribute = MutableLiveData<FilterAttribute>()
    val currentFilterAttribute: LiveData<FilterAttribute>
        get() = _currentFilterAttribute

    var attributes = repository.loadAttributes()

    init {
        _currentFilterAttribute.value = repository.getFilteredAttributes()
        _query.value = CinemaMoviesId(repository.getFilteredAttributes())

    }


    fun loadMovies() {
        val selectedAttributes = repository.getFilteredAttributes()
        val filterAttribute = _currentFilterAttribute.value!!

        if (filterAttribute != selectedAttributes)
            _query.value = CinemaMoviesId(filterAttribute)
    }

    fun setMoviesLanguage(filteredLanguage: String) {
        val filterAttribute = _currentFilterAttribute.value!!
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

    fun setMoviesCinemas(filteredCinemas: String) {
        val filterAttribute = _currentFilterAttribute.value!!
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

    fun getFilteredAttributes() = repository.getFilteredAttributes()

    fun retry() {
        val filterAttribute = _currentFilterAttribute.value!!

        _query.value = CinemaMoviesId(filterAttribute)

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