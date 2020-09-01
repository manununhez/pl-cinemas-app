package com.manudev.cinemaspl.ui.filter

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.manudev.cinemaspl.repository.MovieRepository

class FilterViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {

}