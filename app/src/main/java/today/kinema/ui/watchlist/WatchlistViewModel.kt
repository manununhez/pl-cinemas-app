package today.kinema.ui.watchlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import today.kinema.repository.KinemaRepository
import today.kinema.vo.WatchlistMovie

class WatchlistViewModel @ViewModelInject constructor(
    private val repository: KinemaRepository
) : ViewModel() {

    private val _watchlist = MutableLiveData<List<WatchlistMovie>>()

    val watchlist: LiveData<List<WatchlistMovie>>
        get() = _watchlist

    fun initWatchlist() {
        viewModelScope.launch {
            _watchlist.value =
                repository.getWatchlistMovies(repository.getSortWatchMovieListOrder())
        }
    }

    fun removeWatchlistMovie(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch {
            repository.deleteWatchlistMovie(watchlistMovie)
            initWatchlist()
        }
    }

    fun updateWatchMovieListOrder() {
        val isAsc = repository.getSortWatchMovieListOrder()
        repository.updateWatchMovieListOrder(!isAsc)
        initWatchlist()
    }

}