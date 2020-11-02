package today.kinema.data.db

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import today.kinema.data.*
import today.kinema.data.source.LocalDataSource
import today.kinema.util.AbsentLiveData
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
        private const val SHARED_PREFS_CURRENT_LOCATION = "current_location"
        private const val SHARED_PREFS_MOVIES = "Movies"
        private const val SHARED_PREFS_WATCH_LIST_SORT = "sort watch list"
        private const val SHARED_PREFS_ATTRIBUTES = "Attributes"
        private const val SHARED_PREFS_SELECTED_ATTRIBUTES = "Selected attributes"
        private const val DEFAULT_CITY_CODE = "Warszawa"
        private val DEFAULT_CURRENT_DATE = DateUtils.dateFormat(Date())
    }

    private val watchlistDao = db.watchlistMovieDao()

    /*******************
     * SharedPreferences
     *****************/

    override fun getMovies(filterAttribute: DomainFilterAttribute): LiveData<List<DomainMovie>> {
        val prefsMovie = sharedPreferences.getString(SHARED_PREFS_MOVIES, "")!!
        val prefsAttrs = getFilteredAttributes() == filterAttribute

        if (prefsMovie.isNotEmpty() && prefsAttrs) {
            val type = object : TypeToken<List<RoomMovie>>() {}.type
            val moviesList: List<RoomMovie> = gson.fromJson(prefsMovie, type)

            if (moviesList.isEmpty())
                return AbsentLiveData.create()

            val formattedDay = DateUtils.dateParse(moviesList[0].dateTitle)
            val today = DateUtils.today()

            return if (formattedDay.before(today)) {//if we have a date saved older than today, we update to today's date
                AbsentLiveData.create()
            } else {
                val moviesLiveData = MutableLiveData<List<DomainMovie>>()
                moviesLiveData.value = moviesList.map {
                    it.toDomainMovie()
                }

                return moviesLiveData
            }
        } else {
            return AbsentLiveData.create()
        }
    }

    override fun saveMovies(movies: List<DomainMovie>) {
        val roomMovies = movies.map { it.toRoomMovie() }
        val values = gson.toJson(roomMovies)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_MOVIES, values)
        editor.apply()
    }

    override fun getAttributes(): LiveData<DomainAttribute> {
        val prefs: String = sharedPreferences.getString(SHARED_PREFS_ATTRIBUTES, "")!!
        return if (prefs.isNotEmpty()) {
            val type = object : TypeToken<RoomAttribute>() {}.type
            val locationList: RoomAttribute = gson.fromJson(prefs, type)
            val locationLiveData = MutableLiveData<DomainAttribute>()
            locationLiveData.value = locationList.toDomainAttribute()
            locationLiveData
        } else {
            AbsentLiveData.create()
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
            RoomFilterAttribute(DEFAULT_CITY_CODE, DEFAULT_CURRENT_DATE, listOf(), listOf())
        val prefsAttributes =
            sharedPreferences.getString(
                SHARED_PREFS_SELECTED_ATTRIBUTES,
                gson.toJson(filterAttrDefault)
            )

        val type = object : TypeToken<RoomFilterAttribute>() {}.type
        val filterAttribute: RoomFilterAttribute = gson.fromJson(prefsAttributes, type)

        val formattedDay = DateUtils.dateParse(filterAttribute.date)
        val today = DateUtils.today()

        return if (formattedDay.before(today)) { //if we have a date saved older than today, we update to today's date
            DomainFilterAttribute(
                filterAttribute.city,
                DEFAULT_CURRENT_DATE,
                filterAttribute.cinema,
                filterAttribute.language
            )
        } else {
            filterAttribute.toDomainFilterAttribute()
        }
    }

    override fun saveFilteredAttributes(item: DomainFilterAttribute) {
        val values = gson.toJson(item)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_SELECTED_ATTRIBUTES, values)
        editor.apply()
    }

    override fun getCurrentLocation(): DomainCoordinate {
        val prefsCoordinate = sharedPreferences.getString(
            SHARED_PREFS_CURRENT_LOCATION,
            gson.toJson(RoomCoordinate(0.0, 0.0))
        )
        val type = object : TypeToken<RoomCoordinate>() {}.type
        val coordinate : RoomCoordinate = gson.fromJson(prefsCoordinate, type)
        return coordinate.toDomainCoordinate()
    }

    override fun saveCurrentLocation(currentLocation: DomainCoordinate) {
        val values = gson.toJson(currentLocation)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_CURRENT_LOCATION, values)
        editor.apply()
    }

    override fun getSortWatchList() = sharedPreferences.getBoolean(
        SHARED_PREFS_WATCH_LIST_SORT,
        true
    )

    override fun saveSortWatchList(isAsc: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(SHARED_PREFS_WATCH_LIST_SORT, isAsc)
        editor.apply()
    }

    /********
     * DB
     *******/
    override fun getWatchlistMovies(isAsc: Boolean): LiveData<List<DomainWatchlistMovie>> {
        val watchlistMoviesLiveData: LiveData<List<RoomWatchlistMovie>> =
            watchlistDao.getWatchlistMovies(isAsc)
        return Transformations.map(watchlistMoviesLiveData) { list ->
            list.map {
                it.toDomainWatchlistMovie().apply {
                    header = true
                }
            }
        }
    }

    override suspend fun addWatchlistMovie(watchlistMovie: DomainWatchlistMovie) {
        watchlistDao.insert(watchlistMovie.toRoomWatchlistMovie())
    }

    override suspend fun deleteWatchlistMovie(watchlistMovie: DomainWatchlistMovie) {
        watchlistDao.delete(watchlistMovie.toRoomWatchlistMovie())
    }

    override fun getWatchlistMovie(watchlistMovie: DomainWatchlistMovie): LiveData<DomainWatchlistMovie> {
        val watchlistMovieLiveData: LiveData<RoomWatchlistMovie> =
            watchlistDao.getWatchlistMovie(watchlistMovie.id, watchlistMovie.dateTitle)
        return Transformations.map(watchlistMovieLiveData) {
            it?.toDomainWatchlistMovie()
        }
    }
}