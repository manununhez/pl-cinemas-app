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

    private val _sortOrderWatchList = MutableLiveData<Boolean>()
    val sortOrderWatchList: LiveData<Boolean>
        get() = _sortOrderWatchList

    init {
        initSortOrderWatchlist()
    }

    private fun initSortOrderWatchlist() {
        viewModelScope.launch(mainDispatcher) {
            _sortOrderWatchList.value = repository.getSortWatchMovieListOrder()
        }
    }

    private suspend fun getWatchlistMovies() = withContext(ioDispatcher) {
        repository.getWatchlistMovies(_sortOrderWatchList.value!!)
    }

    private fun removeWatchlistMovie(watchlistMovie: WatchlistMovie) {
        viewModelScope.launch(defaultDispatcher) {
            repository.deleteWatchlistMovie(watchlistMovie)
        }
    }

    fun refreshWatchlist() {
        viewModelScope.launch(mainDispatcher) {
            _watchlist.value = getWatchlistMovies()
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

    fun updateWatchMovieListOrder() {
        val isAsc = !repository.getSortWatchMovieListOrder()
        _sortOrderWatchList.value = isAsc
        repository.updateWatchMovieListOrder(isAsc)
    }

}