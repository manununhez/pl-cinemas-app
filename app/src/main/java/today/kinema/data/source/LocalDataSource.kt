package today.kinema.data.source

import androidx.lifecycle.LiveData
import today.kinema.vo.Coordinate
import today.kinema.vo.FilterAttribute
import today.kinema.vo.WatchlistMovie
import today.kinema.data.api.model.Attribute as ServerAttribute
import today.kinema.vo.Attribute as DomainAttribute
import today.kinema.vo.Movie as DomainMovie


interface LocalDataSource {
    fun getMovies(filterAttribute: FilterAttribute): LiveData<List<DomainMovie>>
    fun saveMovies(movies: List<DomainMovie>)

    fun getAttributes(): LiveData<DomainAttribute>
    fun saveAttributes(item: ServerAttribute)

    fun getFilteredAttributes(): FilterAttribute
    fun saveFilteredAttributes(item: FilterAttribute)

    fun getCurrentLocation(): Coordinate
    fun saveCurrentLocation(currentLocation: Coordinate)

    fun getSortWatchList(): Boolean
    fun saveSortWatchList(isAsc: Boolean)

    fun getWatchlistMovies(isAsc: Boolean): LiveData<List<WatchlistMovie>>
    suspend fun addWatchlistMovie(watchlistMovie: WatchlistMovie)
    suspend fun deleteWatchlistMovie(watchlistMovie: WatchlistMovie)
    fun getWatchlistMovie(watchlistMovie: WatchlistMovie): LiveData<WatchlistMovie>
}