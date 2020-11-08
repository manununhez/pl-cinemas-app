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

        saveFilteredAttributes(
            FilterAttribute(
                filterAttribute.city,
                filterAttribute.date,
                filterAttribute.cinema,
                updateElementInList(filterAttribute.language, filteredLanguage, clearSelection)
            )
        )
    }

    fun setMoviesCinemas(filteredCinemas: String, clearSelection: Boolean) {
        val filterAttribute = _currentFilterAttribute.value!!

        saveFilteredAttributes(
            FilterAttribute(
                filterAttribute.city,
                filterAttribute.date,
                updateElementInList(filterAttribute.cinema, filteredCinemas, clearSelection),
                filterAttribute.language
            )
        )
    }

    fun setMoviesCity(filteredCityName: String) {
        val filterAttribute = _currentFilterAttribute.value!!

        saveFilteredAttributes(
            FilterAttribute(
                filteredCityName,
                filterAttribute.date,
                filterAttribute.cinema,
                filterAttribute.language
            )
        )
    }

    //Open for testing
    fun updateElementInList(
        list: List<String>,
        element: String,
        clearSelection: Boolean
    ): List<String> {
        return if (clearSelection) listOf()
        else {
            val mutableList = list.toMutableList()
            if (mutableList.contains(element)) //checkbox
                mutableList.remove(element) //remove if exists
            else
                mutableList.add(element)//append new cinema to the list

            mutableList
        }
    }

    private fun saveFilteredAttributes(filterAttribute: FilterAttribute) {
        _currentFilterAttribute.value = filterAttribute

        repository.updateFilteredAttributes(filterAttribute)
    }
}