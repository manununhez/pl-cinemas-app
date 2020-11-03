package today.kinema.repository

import today.kinema.data.api.KinemaDataSource
import today.kinema.data.api.Resource
import today.kinema.data.api.model.GeneralResponse
import today.kinema.data.db.RoomDataSource
import today.kinema.data.toDomainMovie
import today.kinema.util.LocationUtils.orderCinemasByDistance
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
        val data: List<DomainMovie> = roomDataSource.getMovies(isAsc)

        if (data.isNotEmpty())//shouldFetch
            return Resource.success(data)

        val result: GeneralResponse<List<ServerMovie>> =
            kinemaDataSource.searchMovies(filterAttribute)

        return if (result.success) {
            //SaveData attributes
            saveSearchMovieParameters(filterAttribute)
            saveFilteredAttributes(filterAttribute)
            //SaveData results
            //order list based on distance
            roomDataSource.saveMovies(
                orderCinemasByDistance(
                    getCurrentLocation(),
                    result.data.map { it.toDomainMovie() })
            )

            //loadFromDb
            Resource.success(roomDataSource.getMovies(isAsc))
        } else {
            Resource.error(result.message, null)
        }

    }

    suspend fun loadAttributes(): Resource<DomainAttribute> {
        val data: DomainAttribute? = roomDataSource.getAttributes()
        if (data != null)//shouldFetch
            return Resource.success(data)

        val result: GeneralResponse<ServerAttribute> = kinemaDataSource.getAttributes()

        return if (result.success) {
            //SaveData
            roomDataSource.saveAttributes(result.data)

            //loadFromDb
            Resource.success(roomDataSource.getAttributes())
        } else {
            Resource.error(result.message, null)
        }

    }

    fun getFilteredAttributes() = roomDataSource.getFilteredAttributes()

    fun saveFilteredAttributes(filterAttribute: DomainFilterAttribute) {
        roomDataSource.saveFilteredAttributes(filterAttribute)
    }

    private fun saveSearchMovieParameters(filterAttribute: DomainFilterAttribute) {
        roomDataSource.saveSearchMovieParameters(filterAttribute)
    }

    fun getCurrentLocation() = roomDataSource.getCurrentLocation()

    fun setCurrentLocation(currentLocation: Coordinate) {
        roomDataSource.saveCurrentLocation(currentLocation)
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

    fun getSortWatchMovieList(): Boolean = roomDataSource.getSortWatchMovieList()

    fun setWatchMovieListOrder(isAsc: Boolean) {
        roomDataSource.saveSortWatchMovieList(isAsc)
    }

    fun getSortMovieList(): Boolean = roomDataSource.getSortMovieList()

    fun updateMovieListOrder(isAsc: Boolean) {
        roomDataSource.saveSortMovieList(isAsc)
    }

}