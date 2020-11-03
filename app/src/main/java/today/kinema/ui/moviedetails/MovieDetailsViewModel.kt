package today.kinema.ui.moviedetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import today.kinema.repository.KinemaRepository
import today.kinema.vo.WatchlistMovie

class MovieDetailsViewModel @ViewModelInject constructor(
    private val repository: KinemaRepository
) : ViewModel() {

    private val _watchlist = MutableLiveData<WatchlistMovie>()

    val watchlist: LiveData<WatchlistMovie>
        get() = _watchlist


    fun getWatchlistMovie(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch {
            _watchlist.value = repository.getWatchlistMovie(watchlistMovie)
        }
    }

    fun addWatchlistMovie(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch {
            repository.addWatchlistMovie(watchlistMovie)
            getWatchlistMovie(watchlistMovie)
        }
    }

    fun removeWatchlistMovie(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch {
            repository.deleteWatchlistMovie(watchlistMovie)
            getWatchlistMovie(watchlistMovie)
        }
    }
}