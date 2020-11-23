package today.kinema.data.source

import today.kinema.vo.Coordinate
import today.kinema.vo.FilterAttribute
import today.kinema.vo.WatchlistMovie
import today.kinema.data.api.model.Attribute as ServerAttribute
import today.kinema.vo.Attribute as DomainAttribute
import today.kinema.vo.Movie as DomainMovie


interface LocalDataSource {
    suspend fun getMovies(isAsc: Boolean): List<DomainMovie>
    suspend fun saveMovies(movies: List<DomainMovie>)
    suspend fun isMoviesNotEmpty(isAsc: Boolean) : Boolean

    fun getAttributes(): DomainAttribute?
    fun saveAttributes(item: ServerAttribute)

    fun getFilteredAttributes(): FilterAttribute
    fun saveFilteredAttributes(item: FilterAttribute)

    fun getSearchMovieParameters(): FilterAttribute
    fun saveSearchMovieParameters(item: FilterAttribute)

    fun getCurrentLocation(): Coordinate
    fun saveCurrentLocation(currentLocation: Coordinate)

    fun getSortMovieList(): Boolean
    fun saveSortMovieList(isAsc: Boolean)

    fun getSortWatchMovieList(): Boolean
    fun saveSortWatchMovieList(isAsc: Boolean)

    suspend fun getWatchlistMovies(isAsc: Boolean): List<WatchlistMovie>
    suspend fun addWatchlistMovie(watchlistMovie: WatchlistMovie)
    suspend fun deleteWatchlistMovie(watchlistMovie: WatchlistMovie)
    suspend fun checkIfWatchMovieExists(watchlistMovie: WatchlistMovie): Boolean
}