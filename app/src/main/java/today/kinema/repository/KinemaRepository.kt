package today.kinema.repository

import today.kinema.data.api.RemoteDataSourceImpl
import today.kinema.data.api.Resource
import today.kinema.data.api.model.GeneralResponse
import today.kinema.data.db.LocalDataSourceImpl
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
    private val remoteDataSourceImpl: RemoteDataSourceImpl,
    private val localDataSourceImpl: LocalDataSourceImpl
) {
    suspend fun loadMovies(
        filterAttribute: DomainFilterAttribute,
        isAsc: Boolean
    ): Resource<List<DomainMovie>> {
        //return from cache
        try {
            val isMoviesNotEmpty = localDataSourceImpl.isMoviesNotEmpty(isAsc)
            if (isMoviesNotEmpty)//shouldFetch
                return Resource.success(localDataSourceImpl.getMovies(isAsc))
        } catch (throwable: Throwable) {
            return Resource.error(throwable.localizedMessage, null)
        }

        //fetch new data
        try {
            val result: GeneralResponse<List<ServerMovie>> =
                remoteDataSourceImpl.searchMovies(filterAttribute)

            return if (result.success) {
                //SaveData attributes
                localDataSourceImpl.saveSearchMovieParameters(filterAttribute)
                updateFilteredAttributes(filterAttribute)
                //SaveData results
                localDataSourceImpl.saveMovies(result.data.map { it.toDomainMovie() })

                //loadFromDb
                Resource.success(localDataSourceImpl.getMovies(isAsc))
            } else {
                Resource.error(result.message, null)
            }
        } catch (throwable: Throwable) {
            return Resource.error(throwable.localizedMessage, null)
        }

    }

    suspend fun loadAttributes(filterAttribute: DomainFilterAttribute): Resource<DomainAttribute> {
        //fetch new data
        try {
            val result: GeneralResponse<ServerAttribute> =
                remoteDataSourceImpl.getAttributes(filterAttribute)

            return if (result.success) {
                //SaveData
                localDataSourceImpl.saveAttributes(result.data)

                //loadFromDb
                return Resource.success(localDataSourceImpl.getAttributes())
            } else {
                Resource.error(result.message, null)
            }
        } catch (throwable: Throwable) {
            //in case of error, we try to show cache data is available
            val savedAttributes = localDataSourceImpl.getAttributes()
            return if (savedAttributes == null)
                Resource.error(throwable.localizedMessage, null)
            else Resource.success(savedAttributes)
        }
    }

    fun getAttributes() = localDataSourceImpl.getAttributes()

    suspend fun getWatchlistMovies(isAsc: Boolean): List<WatchlistMovie> =
        localDataSourceImpl.getWatchlistMovies(isAsc)

    suspend fun checkIfWatchMovieExists(watchlistMovie: WatchlistMovie) =
        localDataSourceImpl.checkIfWatchMovieExists(watchlistMovie)

    suspend fun addWatchlistMovie(watchlistMovie: WatchlistMovie) {
        localDataSourceImpl.addWatchlistMovie(watchlistMovie)
    }

    suspend fun deleteWatchlistMovie(watchlistMovie: WatchlistMovie) {
        localDataSourceImpl.deleteWatchlistMovie(watchlistMovie)
    }

    fun getFilteredAttributes(): DomainFilterAttribute = localDataSourceImpl.getFilteredAttributes()

    fun updateFilteredAttributes(filterAttribute: DomainFilterAttribute) {
        localDataSourceImpl.saveFilteredAttributes(filterAttribute)
    }

    fun getCurrentLocation() = localDataSourceImpl.getCurrentLocation()

    fun updateCurrentLocation(currentLocation: Coordinate) {
        localDataSourceImpl.saveCurrentLocation(currentLocation)
    }

    fun getSortWatchMovieListOrder(): Boolean = localDataSourceImpl.getSortWatchMovieList()

    fun updateWatchMovieListOrder(isAsc: Boolean) {
        localDataSourceImpl.saveSortWatchMovieList(isAsc)
    }

    fun getSortMovieListOrder(): Boolean = localDataSourceImpl.getSortMovieList()

    fun updateMovieListOrder(isAsc: Boolean) {
        localDataSourceImpl.saveSortMovieList(isAsc)
    }

}