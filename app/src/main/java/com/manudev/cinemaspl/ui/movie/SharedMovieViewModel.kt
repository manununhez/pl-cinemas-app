package com.manudev.cinemaspl.ui.movie

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.manudev.cinemaspl.repository.MovieRepository
import com.manudev.cinemaspl.vo.Movies
import com.manudev.cinemaspl.vo.Resource
import com.manudev.cinemaspl.vo.Status.*
import org.jetbrains.annotations.TestOnly

class SharedMovieViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {


    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private val _query = MutableLiveData<String>()
    val query: LiveData<String>
        get() = _query

    val movies: LiveData<Resource<List<Movies>>> =
        Transformations.switchMap(_query) { //isRefreshing ->
//            if (isRefreshing) {
                repository.loadMovies(it)
//            } else AbsentLiveData.create()
        }


    val locations = repository.loadLocations()


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

    fun setMoviesCity(cityName: String){
        if (cityName == _query.value) {
            return
        }
        _query.value = cityName
    }


    fun loadMovies() {
        //TODO refresh data should be done with Date().now or from DB - to avoid refresh same data
        //TODO this function could be renamed to forceRefresh data
        //if the data is not null, we avoid fetching the data again. This should be replaced with DB
        if (movies.value?.data == null) {
            _query.value = ""
        }
    }

    @TestOnly
    fun resetRefreshMovies() {
        _query.value = ""
    }


}