package today.kinema.db

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import today.kinema.util.AbsentLiveData
import today.kinema.util.DateUtils
import today.kinema.vo.Attribute
import today.kinema.vo.Coordinate
import today.kinema.vo.FilterAttribute
import today.kinema.vo.Movies
import java.util.*
import javax.inject.Inject

class LocalStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val db: KinemaDb
) {
    companion object {
        private const val SHARED_PREFS_CURRENT_LOCATION = "current_location"
        private const val SHARED_PREFS_MOVIES = "Movies"
        private const val SHARED_PREFS_WATCH_LIST_SORT = "sort watch list"
        private const val SHARED_PREFS_ATTRIBUTES = "Attributes"
        private const val SHARED_PREFS_SELECTED_ATTRIBUTES = "Selected attributes"
        private const val DEFAULT_CITY_CODE = "Warszawa"
        private val DEFAULT_CURRENT_DATE = DateUtils.dateFormat(Date())
    }

    /*******************
     * SharedPreferences
     *****************/
    fun getMovies(filterAttribute: FilterAttribute): LiveData<List<Movies>> {
        val prefsMovies = sharedPreferences.getString(SHARED_PREFS_MOVIES, "")!!
        val prefsAttrs = getFilteredAttributes() == filterAttribute

        if (prefsMovies.isNotEmpty() && prefsAttrs) {
            val type = object : TypeToken<List<Movies>>() {}.type
            val moviesList: List<Movies> = gson.fromJson(prefsMovies, type)

            if (moviesList.isEmpty())
                return AbsentLiveData.create()

            val formattedDay = DateUtils.dateParse(moviesList[0].movie.dateTitle)
            val today = DateUtils.today()

            return if (formattedDay.before(today)) {//if we have a date saved older than today, we update to today's date
                AbsentLiveData.create()
            } else {
                val moviesLiveData = MutableLiveData<List<Movies>>()
                moviesLiveData.value = moviesList

                return moviesLiveData
            }
        } else {
            return AbsentLiveData.create()
        }
    }

    fun setMovies(movies: List<Movies>) {
        val values = gson.toJson(movies)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_MOVIES, values)
        editor.apply()
    }

    fun getAttributes(): LiveData<Attribute> {
        val prefs: String = sharedPreferences.getString(SHARED_PREFS_ATTRIBUTES, "")!!
        return if (prefs.isNotEmpty()) {
            val type = object : TypeToken<Attribute>() {}.type
            val locationList: Attribute = gson.fromJson(prefs, type)
            val locationLiveData = MutableLiveData<Attribute>()
            locationLiveData.value = locationList
            locationLiveData
        } else {
            AbsentLiveData.create()
        }
    }

    fun setAttributes(item: Attribute) {
        val values = gson.toJson(item)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_ATTRIBUTES, values)
        editor.apply()
    }

    fun getFilteredAttributes(): FilterAttribute {
        val filterAttrDefault =
            FilterAttribute(DEFAULT_CITY_CODE, DEFAULT_CURRENT_DATE, listOf(), listOf())
        val prefsAttributes =
            sharedPreferences.getString(
                SHARED_PREFS_SELECTED_ATTRIBUTES,
                gson.toJson(filterAttrDefault)
            )

        val type = object : TypeToken<FilterAttribute>() {}.type
        val filterAttribute: FilterAttribute = gson.fromJson(prefsAttributes, type)

        val formattedDay = DateUtils.dateParse(filterAttribute.date)
        val today = DateUtils.today()

        return if (formattedDay.before(today)) { //if we have a date saved older than today, we update to today's date
            FilterAttribute(
                filterAttribute.city,
                DEFAULT_CURRENT_DATE,
                filterAttribute.cinema,
                filterAttribute.language
            )
        } else {
            filterAttribute
        }
    }

    fun setFilteredAttributes(item: FilterAttribute) {
        val values = gson.toJson(item)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_SELECTED_ATTRIBUTES, values)
        editor.apply()
    }

    fun getCurrentLocation(): Coordinate {
        val prefsCoordinate = sharedPreferences.getString(
            SHARED_PREFS_CURRENT_LOCATION,
            gson.toJson(Coordinate(0.0, 0.0))
        )
        val type = object : TypeToken<Coordinate>() {}.type
        return gson.fromJson(prefsCoordinate, type)
    }

    fun setCurrentLocation(currentLocation: Coordinate) {
        val values = gson.toJson(currentLocation)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_CURRENT_LOCATION, values)
        editor.apply()
    }

    fun getSortWatchList() = sharedPreferences.getBoolean(
        SHARED_PREFS_WATCH_LIST_SORT,
        true
    )

    fun setSortWatchList(isAsc: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(SHARED_PREFS_WATCH_LIST_SORT, isAsc)
        editor.apply()
    }

    /********
     * DB
     *******/
    fun getWatchlistMovies(isAsc: Boolean) = db.watchlistMovieDao().getWatchlistMovies(isAsc)

    suspend fun addWatchlistMovie(watchlistMovie: WatchlistMovie) {
        db.watchlistMovieDao().insert(watchlistMovie)
    }

    suspend fun deleteWatchlistMovie(watchlistMovie: WatchlistMovie) {
        db.watchlistMovieDao().delete(watchlistMovie)
    }

    fun getWatchlistMovie(watchlistMovie: WatchlistMovie) =
        db.watchlistMovieDao().getWatchlistMovie(watchlistMovie.id, watchlistMovie.dateTitle)


}