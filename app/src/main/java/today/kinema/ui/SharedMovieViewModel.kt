package today.kinema.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import today.kinema.repository.KinemaRepository
import today.kinema.vo.Coordinate

class SharedMovieViewModel @ViewModelInject constructor(
    private val repository: KinemaRepository
) : ViewModel() {

    fun setCurrentLocation(currentLocation: Coordinate) {
        repository.updateCurrentLocation(currentLocation)
    }
}