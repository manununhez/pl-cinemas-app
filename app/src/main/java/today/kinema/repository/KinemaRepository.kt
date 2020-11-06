package today.kinema.repository

import today.kinema.data.api.KinemaDataSource
import today.kinema.data.api.Resource
import today.kinema.data.api.model.GeneralResponse
import today.kinema.data.db.RoomDataSource
import today.kinema.data.toDomainMovie
import today.kinema.vo.Coordinate
import today.kinema.vo.WatchlistMovie
import javax.inject.Inject
import today.kinema.data.api.model.Attribute as ServerAttribute
import today.kinema.data.api.model.Movie as ServerMovie
import today.kinema.vo.Attribute as DomainAttribute
import today.kinema.vo.FilterAttribute as DomainFilterAttribute
import today.kinema.vo.Movie as DomainMovie

class KinemaRepository @Inject constructor(
    private val kinemaDataSource: KinemaDataSource,
    private val roomDataSource: RoomDataSource
) {

    suspend fun loadMovies(
        filterAttribute: DomainFilterAttribute,
        isAsc: Boolean
    ): Resource<List<DomainMovie>> {

        if (roomDataSource.isMoviesNotEmpty(isAsc))//shouldFetch
            return Resource.success(roomDataSource.getMovies(isAsc))

        val result: GeneralResponse<List<ServerMovie>> =
            kinemaDataSource.searchMovies(filterAttribute)

        return if (result.success) {
            //SaveData attributes
            roomDataSource.saveSearchMovieParameters(filterAttribute)
            updateFilteredAttributes(filterAttribute)
            //SaveData results
            roomDataSource.saveMovies(result.data.map { it.toDomainMovie() })

            //loadFromDb
            Resource.success(roomDataSource.getMovies(isAsc))
        } else {
            Resource.error(result.message, null)
        }

    }

    suspend fun loadAttributes(filterAttribute: DomainFilterAttribute,): Resource<DomainAttribute> {
        val result: GeneralResponse<ServerAttribute> = kinemaDataSource.getAttributes(filterAttribute)

        return if (result.success) {
            //SaveData
            roomDataSource.saveAttributes(result.data)

            //loadFromDb
            Resource.success(roomDataSource.getAttributes())
        } else {
            Resource.error(result.message, null)
        }
    }

    suspend fun getWatchlistMovies(isAsc: Boolean): List<WatchlistMovie> =
        roomDataSource.getWatchlistMovies(isAsc)

    suspend fun getWatchlistMovie(watchlistMovie: WatchlistMovie): WatchlistMovie? =
        roomDataSource.getWatchlistMovie(watchlistMovie)

    suspend fun addWatchlistMovie(watchlistMovie: WatchlistMovie) {
        roomDataSource.addWatchlistMovie(watchlistMovie)
    }

    suspend fun deleteWatchlistMovie(watchlistMovie: WatchlistMovie) {
        roomDataSource.deleteWatchlistMovie(watchlistMovie)
    }

    fun getFilteredAttributes(): DomainFilterAttribute = roomDataSource.getFilteredAttributes()

    fun updateFilteredAttributes(filterAttribute: DomainFilterAttribute) {
        roomDataSource.saveFilteredAttributes(filterAttribute)
    }

    fun getCurrentLocation() = roomDataSource.getCurrentLocation()

    fun updateCurrentLocation(currentLocation: Coordinate) {
        roomDataSource.saveCurrentLocation(currentLocation)
    }

    fun getSortWatchMovieListOrder(): Boolean = roomDataSource.getSortWatchMovieList()

    fun updateWatchMovieListOrder(isAsc: Boolean) {
        roomDataSource.saveSortWatchMovieList(isAsc)
    }

    fun getSortMovieListOrder(): Boolean = roomDataSource.getSortMovieList()

    fun updateMovieListOrder(isAsc: Boolean) {
        roomDataSource.saveSortMovieList(isAsc)
    }

}