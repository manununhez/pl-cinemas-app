package today.kinema.data.db

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import today.kinema.data.*
import today.kinema.data.source.LocalDataSource
import today.kinema.util.DateUtils
import java.util.*
import javax.inject.Inject
import today.kinema.data.api.model.Attribute as ServerAttribute
import today.kinema.data.db.model.Attribute as RoomAttribute
import today.kinema.data.db.model.Coordinate as RoomCoordinate
import today.kinema.data.db.model.FilterAttribute as RoomFilterAttribute
import today.kinema.data.db.model.Movie as RoomMovie
import today.kinema.data.db.model.WatchlistMovie as RoomWatchlistMovie
import today.kinema.vo.Attribute as DomainAttribute
import today.kinema.vo.Coordinate as DomainCoordinate
import today.kinema.vo.FilterAttribute as DomainFilterAttribute
import today.kinema.vo.Movie as DomainMovie
import today.kinema.vo.WatchlistMovie as DomainWatchlistMovie

class RoomDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    db: KinemaDb
) : LocalDataSource {
    companion object {
        private const val SHARED_PREFS_CURRENT_LOCATION = "SHARED_PREFS_CURRENT_LOCATION"
        private const val SHARED_PREFS_MOVIES_LIST_SORT = "SHARED_PREFS_MOVIES_LIST_SORT"
        private const val SHARED_PREFS_WATCH_LIST_SORT = "SHARED_PREFS_WATCH_LIST_SORT"
        private const val SHARED_PREFS_ATTRIBUTES = "SHARED_PREFS_ATTRIBUTES"
        private const val SHARED_PREFS_FILTERED_ATTRIBUTES = "SHARED_PREFS_FILTERED_ATTRIBUTES"
        private const val SHARED_PREFS_SEARCH_MOVIE_ATTRIBUTES =
            "SHARED_PREFS_SEARCH_MOVIE_ATTRIBUTES"
        private val DEFAULT_CURRENT_DATE = DateUtils.dateFormat(Date())

    }

    private val watchlistDao = db.watchlistMovieDao()
    private val movieDao = db.movieDao()

    /*******************
     * SharedPreferences
     *****************/

    override fun getAttributes(): DomainAttribute? {
        val prefs: String = sharedPreferences.getString(SHARED_PREFS_ATTRIBUTES, "")!!
        return if (prefs.isNotEmpty()) {
            val type = object : TypeToken<RoomAttribute>() {}.type
            val locationList: RoomAttribute = gson.fromJson(prefs, type)
            locationList.toDomainAttribute()
        } else {
            null
        }
    }

    override fun saveAttributes(item: ServerAttribute) {
        val roomAttribute = item.toRoomAttribute()
        val values = gson.toJson(roomAttribute)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_ATTRIBUTES, values)
        editor.apply()
    }

    override fun getFilteredAttributes(): DomainFilterAttribute {
        val filterAttrDefault =
            DomainFilterAttribute().toRoomFilterAttribute()
        val prefsAttributes =
            sharedPreferences.getString(
                SHARED_PREFS_FILTERED_ATTRIBUTES,
                gson.toJson(filterAttrDefault)
            )

        val type = object : TypeToken<RoomFilterAttribute>() {}.type
        val filterAttribute: RoomFilterAttribute = gson.fromJson(prefsAttributes, type)

        val formattedDay = DateUtils.dateParse(filterAttribute.date)
        val today = DateUtils.today()

        val domainFilteredAttributes = filterAttribute.toDomainFilterAttribute()
        return if (formattedDay.before(today)) { //if we have a date saved older than today, we update to today's date
            domainFilteredAttributes.date = DEFAULT_CURRENT_DATE
            domainFilteredAttributes
        } else {
            domainFilteredAttributes
        }
    }

    override fun saveFilteredAttributes(item: DomainFilterAttribute) {
        val values = gson.toJson(item)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_FILTERED_ATTRIBUTES, values)
        editor.apply()
    }

    override fun getSearchMovieParameters(): DomainFilterAttribute {
        val filterAttrDefault =
            DomainFilterAttribute().toRoomFilterAttribute()
        val prefsAttributes =
            sharedPreferences.getString(
                SHARED_PREFS_SEARCH_MOVIE_ATTRIBUTES,
                gson.toJson(filterAttrDefault)
            )

        val type = object : TypeToken<RoomFilterAttribute>() {}.type
        val filterAttribute: RoomFilterAttribute = gson.fromJson(prefsAttributes, type)

        return filterAttribute.toDomainFilterAttribute()
    }

    override fun saveSearchMovieParameters(item: DomainFilterAttribute) {
        val values = gson.toJson(item)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_SEARCH_MOVIE_ATTRIBUTES, values)
        editor.apply()
    }

    override fun getCurrentLocation(): DomainCoordinate {
        val prefsCoordinate = sharedPreferences.getString(
            SHARED_PREFS_CURRENT_LOCATION,
            gson.toJson(RoomCoordinate(0.0, 0.0))
        )
        val type = object : TypeToken<RoomCoordinate>() {}.type
        val coordinate: RoomCoordinate = gson.fromJson(prefsCoordinate, type)
        return coordinate.toDomainCoordinate()
    }

    override fun saveCurrentLocation(currentLocation: DomainCoordinate) {
        val values = gson.toJson(currentLocation)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_CURRENT_LOCATION, values)
        editor.apply()
    }

    override fun getSortWatchMovieList() = sharedPreferences.getBoolean(
        SHARED_PREFS_WATCH_LIST_SORT,
        true
    )

    override fun saveSortWatchMovieList(isAsc: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(SHARED_PREFS_WATCH_LIST_SORT, isAsc)
        editor.apply()
    }

    override fun getSortMovieList() = sharedPreferences.getBoolean(
        SHARED_PREFS_MOVIES_LIST_SORT,
        true
    )

    override fun saveSortMovieList(isAsc: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(SHARED_PREFS_MOVIES_LIST_SORT, isAsc)
        editor.apply()
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

    override suspend fun getWatchlistMovie(watchlistMovie: DomainWatchlistMovie): DomainWatchlistMovie? {
        val watchlistMovieLiveData: RoomWatchlistMovie? =
            watchlistDao.getWatchlistMovie(watchlistMovie.id, watchlistMovie.dateTitle)
        return watchlistMovieLiveData?.toDomainWatchlistMovie()
    }

    override suspend fun getMovies(isAsc: Boolean): List<DomainMovie> {
        val prefsAttrs = getFilteredAttributes() == getSearchMovieParameters()
        val movie: RoomMovie? = movieDao.getFirstMovie()

        if (movie != null && prefsAttrs) {
            val formattedDay = DateUtils.dateParse(movie.dateTitle)
            val today = DateUtils.today()

            return if (formattedDay.before(today)) {//if we have a date saved older than today, we update to today's date
                listOf()
            } else {
                val moviesList = movieDao.getMovies(isAsc)
                moviesList.map {
                    it.toDomainMovie()
                }
            }
        } else {
            return listOf()
        }
    }

    override suspend fun saveMovies(movies: List<DomainMovie>) {
        val roomMovies = movies.map { it.toRoomMovie() }
        movieDao.clear()
        movieDao.insertMovies(roomMovies)
    }
}