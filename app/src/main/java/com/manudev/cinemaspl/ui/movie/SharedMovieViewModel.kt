package com.manudev.cinemaspl.ui.movie

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.manudev.cinemaspl.repository.MovieRepository
import com.manudev.cinemaspl.util.AbsentLiveData
import com.manudev.cinemaspl.vo.Movies
import com.manudev.cinemaspl.vo.Resource
import com.manudev.cinemaspl.vo.Status.*

class SharedMovieViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    companion object {
        val TAG = SharedMovieViewModel::class.java.simpleName
    }

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private val _query = MutableLiveData<CinemaMoviesId>()
    val query: LiveData<CinemaMoviesId>
        get() = _query

    val movies: LiveData<Resource<List<Movies>>> =
        switchMap(_query) { input ->
            input.ifExists { city, date ->
                repository.loadMovies(city, date)
            }
        }


    val locations = repository.loadLocations()

    val date = repository.loadDates()

    fun init() {
        _loading.value = true
        _error.value = false

        //TODO load and error should be DataBinding???
        movies.observeForever {
            it?.let {
                when (it.status) {
                    LOADING -> {
                        _loading.value = true
                        _error.value = false
                    }
                    SUCCESS -> {
                        _loading.value = false
                        _error.value = false
                    }
                    ERROR -> {
                        _loading.value = false
                        _error.value = true
                    }
                }
            }
        }
    }

    fun setMoviesCity(newCityName: String) {
        val originalCity = _query.value?.city
        val originalDate = _query.value?.date ?: repository.getSelectedDate()
        if (newCityName == originalCity) {
            return
        }
        _query.value = CinemaMoviesId(newCityName, originalDate)
    }

    fun setDateMoviesTitle(newDate: String) {
        val originalCity = _query.value?.city ?: repository.getSelectedCity()
        val originalDate = _query.value?.date
        if (newDate == originalDate) {
            return
        }
        _query.value = CinemaMoviesId(originalCity, newDate)
    }


    fun loadMovies() {
        //TODO refresh data should be done with Date().now or from DB - to avoid refresh same data
        //TODO this function could be renamed to forceRefresh data
        //if the data is not null, we avoid fetching the data again. This should be replaced with DB
        Log.d(TAG, "loadMovies()")
        _query.value =
            CinemaMoviesId(repository.getSelectedCity(), repository.getSelectedDate())
    }

    fun retry() {
        val originalCity = _query.value?.city
        val originalDate = _query.value?.date
        if (originalCity != null && originalDate != null) {
            _query.value = CinemaMoviesId(originalCity, originalDate)
        }
    }

//    @TestOnly
//    fun resetRefreshMovies() {
//        _query.value = ""
//    }

    data class CinemaMoviesId(val city: String, val date: String) {
        fun <T> ifExists(f: (String, String) -> LiveData<T>): LiveData<T> {
            return if (city.isBlank() || date.isBlank()) {
                AbsentLiveData.create()
            } else {
                f(city, date)
            }
        }
    }


}