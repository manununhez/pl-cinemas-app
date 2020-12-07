package today.kinema.data.db

import today.kinema.data.*
import today.kinema.data.db.SharedPreferencesDB.Companion.DEFAULT_CURRENT_DATE
import today.kinema.data.source.LocalDataSource
import today.kinema.util.DateUtils
import today.kinema.vo.Coordinate
import today.kinema.vo.FilterAttribute
import javax.inject.Inject
import today.kinema.data.api.model.Attribute as ServerAttribute
import today.kinema.data.db.model.WatchlistMovie as RoomWatchlistMovie
import today.kinema.vo.Movie as DomainMovie
import today.kinema.vo.WatchlistMovie as DomainWatchlistMovie

class LocalDataSourceImpl @Inject constructor(
    private val sharedPreferencesDB: SharedPreferencesDB,
    private val watchlistDao: WatchlistMovieDao,
    private val movieDao: MovieDao
) : LocalDataSource {

    /*******************
     * SharedPreferences
     *****************/

    override fun getAttributes() = sharedPreferencesDB.getAttributes()?.toDomainAttribute()

    override fun saveAttributes(item: ServerAttribute) {
        sharedPreferencesDB.saveAttributes(item.toRoomAttribute())
    }

    override fun getFilteredAttributes(): FilterAttribute {
        val filterAttribute = sharedPreferencesDB.getFilteredAttributes()

        val formattedDay = DateUtils.dateParse(filterAttribute.date)
        val today = DateUtils.today()

        val domainFilteredAttributes = filterAttribute.toDomainFilterAttribute()
        return if (formattedDay.before(today)) { //if we have a date saved older than today, we update to today's date
            domainFilteredAttributes.copy(date = DEFAULT_CURRENT_DATE)
        } else {
            domainFilteredAttributes
        }
    }

    override fun saveFilteredAttributes(item: FilterAttribute) {
        sharedPreferencesDB.saveFilteredAttributes(item.toRoomFilterAttribute())
    }

    override fun getSearchMovieParameters() =
        sharedPreferencesDB.getSearchMovieParameters().toDomainFilterAttribute()

    override fun saveSearchMovieParameters(item: FilterAttribute) {
        sharedPreferencesDB.saveSearchMovieParameters(item.toRoomFilterAttribute())
    }

    override fun getCurrentLocation() =
        sharedPreferencesDB.getCurrentLocation().toDomainCoordinate()

    override fun saveCurrentLocation(currentLocation: Coordinate) {
        sharedPreferencesDB.saveCurrentLocation(currentLocation.toDomainCoordinate())
    }

    override fun getSortWatchMovieList() = sharedPreferencesDB.getSortWatchMovieList()

    override fun saveSortWatchMovieList(isAsc: Boolean) {
        sharedPreferencesDB.saveSortWatchMovieList(isAsc)
    }

    override fun getSortMovieList() = sharedPreferencesDB.getSortMovieList()

    override fun saveSortMovieList(isAsc: Boolean) {
        sharedPreferencesDB.saveSortMovieList(isAsc)
    }

    /********
     * DB
     *******/
    override suspend fun getWatchlistMovies(isAsc: Boolean): List<DomainWatchlistMovie> {
        val watchlistMoviesLiveData: List<RoomWatchlistMovie> =
            watchlistDao.getWatchlistMovies(isAsc)
        return watchlistMoviesLiveData.map {
            it.toDomainWatchlistMovie()
        }
    }

    override suspend fun addWatchlistMovie(watchlistMovie: DomainWatchlistMovie) {
        watchlistDao.insert(watchlistMovie.toRoomWatchlistMovie())
    }

    override suspend fun deleteWatchlistMovie(watchlistMovie: DomainWatchlistMovie) {
        watchlistDao.delete(watchlistMovie.toRoomWatchlistMovie())
    }

    override suspend fun checkIfWatchMovieExists(watchlistMovie: DomainWatchlistMovie) =
        watchlistDao.checkIfWatchMovieExists(watchlistMovie.id, watchlistMovie.dateTitle) > 0

    override suspend fun isMoviesNotEmpty(isAsc: Boolean) = getMovies(isAsc).isNotEmpty()

    override suspend fun getMovies(isAsc: Boolean): List<DomainMovie> {
        val moviesList = movieDao.getMovies(isAsc)
        val prefsAttrs = getFilteredAttributes() == getSearchMovieParameters()

        if (moviesList.isEmpty() || !prefsAttrs) return listOf()

        val formattedDay = DateUtils.dateParse(moviesList[0].dateTitle)
        val today = DateUtils.today()

        return if (formattedDay.before(today)) {//if we have a date saved older than today, we update to today's date
            listOf()
        } else {
            moviesList.map {
                it.toDomainMovie()
            }
        }
    }

    override suspend fun saveMovies(movies: List<DomainMovie>) {
        val roomMovies = movies.map { it.toRoomMovie() }
        movieDao.clear()
        movieDao.insertMovies(roomMovies)
    }
}