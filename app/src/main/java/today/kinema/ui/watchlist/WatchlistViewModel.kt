package today.kinema.ui.watchlist

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
import today.kinema.vo.WatchlistMovie

class WatchlistViewModel @ViewModelInject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val repository: KinemaRepository
) : ViewModel() {

    private val _watchlist = MutableLiveData<List<WatchlistMovie>>()
    val watchlist: LiveData<List<WatchlistMovie>>
        get() = _watchlist

    private val _sortOrderList = MutableLiveData<Boolean>()
    val sortOrderList: LiveData<Boolean>
        get() = _sortOrderList

    init {
        _sortOrderList.value = repository.getSortWatchMovieListOrder()
    }

    fun refreshWatchlist() {
        viewModelScope.launch(mainDispatcher) {
            val isAsc = _sortOrderList.value!!
            val list = withContext(ioDispatcher) { repository.getWatchlistMovies(isAsc) }
            _watchlist.value = list
        }
    }

    fun onRemoveWatchlistBtnClicked(watchlistMovie: WatchlistMovie) {
        removeWatchlistMovie(watchlistMovie)
        refreshWatchlist()
    }

    fun onSortMovieWatchlistBtnClicked() {
        updateWatchMovieListOrder()
        refreshWatchlist()
    }

    private fun removeWatchlistMovie(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch(defaultDispatcher) {
            repository.deleteWatchlistMovie(watchlistMovie)
        }
    }

    fun updateWatchMovieListOrder() {
        val isAsc = !repository.getSortWatchMovieListOrder()
        _sortOrderList.value = isAsc
        repository.updateWatchMovieListOrder(isAsc)
    }

}