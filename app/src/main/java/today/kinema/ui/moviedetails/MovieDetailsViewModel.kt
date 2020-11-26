package today.kinema.ui.moviedetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import today.kinema.di.DefaultDispatcher
import today.kinema.di.IoDispatcher
import today.kinema.di.MainDispatcher
import today.kinema.repository.KinemaRepository
import today.kinema.util.LocationUtils
import today.kinema.vo.Cinema
import today.kinema.vo.WatchlistMovie

/**
 * In order to make the dispatcher using in production code
 * the same as in the unit test, we inject the dispatcher like
 */
class MovieDetailsViewModel @ViewModelInject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val repository: KinemaRepository
) : ViewModel() {

    private val _watchlist = MutableLiveData<Boolean>()
    private val _cinemas = MutableLiveData<List<Cinema>>()

    val watchlist: LiveData<Boolean>
        get() = _watchlist

    val cinemas: LiveData<List<Cinema>>
        get() = _cinemas

    fun onAddWatchlistBtnClicked(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch(defaultDispatcher) {
            repository.addWatchlistMovie(watchlistMovie)
            refreshWatchlist(watchlistMovie)
        }
    }

    fun onRemoveWatchlistBtnClicked(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch(defaultDispatcher) {
            repository.deleteWatchlistMovie(watchlistMovie)
            refreshWatchlist(watchlistMovie)
        }
    }

    fun refreshWatchlist(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch(mainDispatcher) {
            _watchlist.value = repository.checkIfWatchMovieExists(watchlistMovie)
        }
    }

    /**
     * We sort the cinemas in IO context
     */
    fun orderCinemasByDistance(cinemas: List<Cinema>) {
        viewModelScope.launch(ioDispatcher) {
            withContext(ioDispatcher) {
                LocationUtils.orderCinemasByDistance(
                    repository.getCurrentLocation(),
                    cinemas
                )
            }.let {
                withContext(mainDispatcher) {
                    _cinemas.value = it
                }
            }
        }
    }
}