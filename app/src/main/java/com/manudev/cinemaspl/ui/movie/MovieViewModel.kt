package com.manudev.cinemaspl.ui.movie

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.manudev.cinemaspl.repository.MovieRepository
import com.manudev.cinemaspl.util.AbsentLiveData
import com.manudev.cinemaspl.vo.Movies
import com.manudev.cinemaspl.vo.Resource
import com.manudev.cinemaspl.vo.Status.*
import org.jetbrains.annotations.TestOnly

class MovieViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {


    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private val _loadTrigger = MutableLiveData<Boolean>()
    val loadTrigger: LiveData<Boolean>
        get() = _loadTrigger

    val movies: LiveData<Resource<List<Movies>>> =
        Transformations.switchMap(_loadTrigger) { isRefreshing ->
            if (isRefreshing) {
//                _loadTrigger.value = false
                repository.loadMovies()
            } else AbsentLiveData.create()
        }


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
                    ERROR -> { //ERROR
                        _loading.value = false
                        _error.value = true
                    }
                }
            }
        }

    }


    fun refreshMovies() {
        //TODO refresh data should be done with Date().now or from DB - to avoid refresh same data
        //TODO this function could be renamed to forceRefresh data
        _loadTrigger.value = true
    }

    @TestOnly
    fun resetRefreshMovies() {
        _loadTrigger.value = false
    }


}