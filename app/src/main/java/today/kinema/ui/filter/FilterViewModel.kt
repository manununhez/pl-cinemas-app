package today.kinema.ui.filter

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import today.kinema.repository.KinemaRepository
import today.kinema.vo.FilterAttribute

class FilterViewModel @ViewModelInject constructor(
    private val repository: KinemaRepository
) : ViewModel() {

    private val _currentFilterAttribute = MutableLiveData<FilterAttribute>()

    val currentFilterAttribute: LiveData<FilterAttribute>
        get() = _currentFilterAttribute

    init {
        _currentFilterAttribute.value = repository.getFilteredAttributes()
    }

    fun setMoviesLanguage(filteredLanguage: String, clearSelection: Boolean) {
        val filterAttribute = _currentFilterAttribute.value!!

        if (clearSelection) {
            filterAttribute.language = listOf()
        } else {

            val languagesList = filterAttribute.language.toMutableList()
            if (languagesList.contains(filteredLanguage)) //checkbox
                languagesList.remove(filteredLanguage) //remove if exists
            else
                languagesList.add(filteredLanguage)//append new cinema to the list

            filterAttribute.language = languagesList
        }

        _currentFilterAttribute.value = filterAttribute
        saveFilteredAttributes()
    }

    fun setMoviesCinemas(filteredCinemas: String, clearSelection: Boolean) {
        val filterAttribute = _currentFilterAttribute.value!!

        if (clearSelection) {
            filterAttribute.cinema = listOf()
        } else {
            val cinemasList = filterAttribute.cinema.toMutableList()
            if (cinemasList.contains(filteredCinemas)) //checkbox
                cinemasList.remove(filteredCinemas) //remove if exists
            else
                cinemasList.add(filteredCinemas)//append new cinema to the list

            filterAttribute.cinema = cinemasList

        }
        _currentFilterAttribute.value = filterAttribute
        saveFilteredAttributes()
    }

    fun setMoviesCity(filteredCityName: String) {
        val filterAttribute = _currentFilterAttribute.value!!

        filterAttribute.city = filteredCityName

        _currentFilterAttribute.value = filterAttribute
        saveFilteredAttributes()
    }

    private fun saveFilteredAttributes() {
        repository.saveFilteredAttributes(_currentFilterAttribute.value!!)
    }
}