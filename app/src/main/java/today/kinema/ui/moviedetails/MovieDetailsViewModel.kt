package today.kinema.ui.moviedetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import today.kinema.repository.KinemaRepository
import today.kinema.util.LocationUtils
import today.kinema.vo.Cinema
import today.kinema.vo.WatchlistMovie

class MovieDetailsViewModel @ViewModelInject constructor(
    private val repository: KinemaRepository
) : ViewModel() {

    private val _watchlist = MutableLiveData<WatchlistMovie>()
    private val _cinemas = MutableLiveData<List<Cinema>>()

    val watchlist: LiveData<WatchlistMovie>
        get() = _watchlist

    val cinemas: LiveData<List<Cinema>>
        get() = _cinemas


    fun refreshWatchlistMovie(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch {
            _watchlist.value = repository.getWatchlistMovie(watchlistMovie)
        }
    }

    fun onAddWatchlistBtnClicked(watchlistMovie: WatchlistMovie) {
        addWatchlistMovie(watchlistMovie)
        refreshWatchlistMovie(watchlistMovie)
    }

    fun onRemoveWatchlistBtnClicked(watchlistMovie: WatchlistMovie) {
        removeWatchlistMovie(watchlistMovie)
        refreshWatchlistMovie(watchlistMovie)
    }

    private fun addWatchlistMovie(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch {
            repository.addWatchlistMovie(watchlistMovie)
        }
    }

    private fun removeWatchlistMovie(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch {
            repository.deleteWatchlistMovie(watchlistMovie)
        }
    }

    fun orderCinemasByDistance(cinemas: List<Cinema>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                LocationUtils.orderCinemasByDistance(
                    repository.getCurrentLocation(),
                    cinemas
                )
            }.let {
                withContext(Dispatchers.Main) {
                    _cinemas.value = it
                }
            }
        }
    }

}