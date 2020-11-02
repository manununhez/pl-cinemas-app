package today.kinema.repository

import androidx.lifecycle.LiveData
import today.kinema.data.api.ApiResponse
import today.kinema.data.api.KinemaDataSource
import today.kinema.data.api.model.GeneralResponse
import today.kinema.data.db.RoomDataSource
import today.kinema.data.toDomainMovie
import today.kinema.util.AppExecutors
import today.kinema.util.LocationUtils.orderCinemasByDistance
import today.kinema.vo.Coordinate
import today.kinema.vo.WatchlistMovie
import javax.inject.Inject
import today.kinema.data.api.model.Attribute as ServerAttribute
import today.kinema.data.api.model.Movie as ServerMovie
import today.kinema.vo.Attribute as DomainAttribute
import today.kinema.vo.FilterAttribute as DomainFilterAttribute
import today.kinema.vo.Movie as DomainMovie

class MovieRepository @Inject constructor(
    private val kinemaDataSource: KinemaDataSource,
    private val roomDataSource: RoomDataSource,
    private val appExecutors: AppExecutors
) {

    fun loadMovies(filterAttribute: DomainFilterAttribute) =
        object : NetworkBoundResource<List<DomainMovie>, List<ServerMovie>>(appExecutors) {
            override fun saveCallResult(item: List<ServerMovie>) {
                roomDataSource.saveFilteredAttributes(filterAttribute)
                //order list based on distance
                roomDataSource.saveMovies(
                    orderCinemasByDistance(getCurrentLocation(), item.map { it.toDomainMovie() })
                )
            }

            override fun shouldFetch(data: List<DomainMovie>?): Boolean =
                (data == null || data.isEmpty())

            override fun loadFromDb(): LiveData<List<DomainMovie>> =
                roomDataSource.getMovies(filterAttribute)

            override fun createCall(): LiveData<ApiResponse<GeneralResponse<List<ServerMovie>>>> =
                kinemaDataSource.searchMovies(filterAttribute)

        }.asLiveData()

    fun loadAttributes() =
        object : NetworkBoundResource<DomainAttribute, ServerAttribute>(appExecutors) {

            override fun saveCallResult(item: ServerAttribute) {
                roomDataSource.saveAttributes(item)
            }

            override fun shouldFetch(data: DomainAttribute?): Boolean =
                true//(data == null) //TODO change when a storage mechanism is added

            override fun loadFromDb(): LiveData<DomainAttribute> = roomDataSource.getAttributes()

            override fun createCall(): LiveData<ApiResponse<GeneralResponse<ServerAttribute>>> =
                kinemaDataSource.getAttributes()
        }.asLiveData()

    fun getFilteredAttributes() = roomDataSource.getFilteredAttributes()

    fun getCurrentLocation() = roomDataSource.getCurrentLocation()

    fun setCurrentLocation(currentLocation: Coordinate) {
        roomDataSource.saveCurrentLocation(currentLocation)
    }

    fun getWatchlistMovies(isAsc: Boolean): LiveData<List<WatchlistMovie>> =
        roomDataSource.getWatchlistMovies(isAsc)

    fun getWatchlistMovie(watchlistMovie: WatchlistMovie): LiveData<WatchlistMovie> =
        roomDataSource.getWatchlistMovie(watchlistMovie)

    suspend fun addWatchlistMovie(watchlistMovie: WatchlistMovie) {
        roomDataSource.addWatchlistMovie(watchlistMovie)
    }

    suspend fun deleteWatchlistMovie(watchlistMovie: WatchlistMovie) {
        roomDataSource.deleteWatchlistMovie(watchlistMovie)
    }

    fun getSortWatchList(): Boolean = roomDataSource.getSortWatchList()

    fun setWatchListOrder(isAsc: Boolean) {
        roomDataSource.saveSortWatchList(isAsc)
    }

}