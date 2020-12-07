package today.kinema.data.db

import android.content.SharedPreferences
import com.google.gson.Gson
import today.kinema.data.toRoomFilterAttribute
import today.kinema.util.DateUtils
import java.util.*
import javax.inject.Inject
import today.kinema.data.db.model.Attribute as RoomAttribute
import today.kinema.data.db.model.Coordinate as RoomCoordinate
import today.kinema.data.db.model.FilterAttribute as RoomFilterAttribute
import today.kinema.vo.FilterAttribute as DomainFilterAttribute

class SharedPreferencesDB @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    companion object {
        private const val SHARED_PREFS_CURRENT_LOCATION = "SHARED_PREFS_CURRENT_LOCATION"
        private const val SHARED_PREFS_MOVIES_LIST_SORT = "SHARED_PREFS_MOVIES_LIST_SORT"
        private const val SHARED_PREFS_WATCH_LIST_SORT = "SHARED_PREFS_WATCH_LIST_SORT"
        private const val SHARED_PREFS_ATTRIBUTES = "SHARED_PREFS_ATTRIBUTES"
        private const val SHARED_PREFS_FILTERED_ATTRIBUTES = "SHARED_PREFS_FILTERED_ATTRIBUTES"
        private const val SHARED_PREFS_SEARCH_MOVIE_ATTRIBUTES =
            "SHARED_PREFS_SEARCH_MOVIE_ATTRIBUTES"
        private const val DEFAULT_CITY_CODE = "Warszawa"
        val DEFAULT_CURRENT_DATE = DateUtils.dateFormat(Date())

    }

    private val defaultFilterAttribute = DomainFilterAttribute(
        DEFAULT_CITY_CODE,
        DEFAULT_CURRENT_DATE,
        listOf(),
        listOf()
    ).toRoomFilterAttribute()
    private val defaultCoordinate = RoomCoordinate(0.0, 0.0)

    /*******************
     * SharedPreferences
     *****************/

    fun getAttributes(): RoomAttribute? {
        val prefs: String = sharedPreferences.getString(SHARED_PREFS_ATTRIBUTES, "")!!
        return if (prefs.isEmpty())
            null
        else gson.fromJson(prefs, RoomAttribute::class.java)
    }

    fun saveAttributes(item: RoomAttribute) {
        val values = gson.toJson(item)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_ATTRIBUTES, values)
        editor.apply()
    }

    fun getFilteredAttributes(): RoomFilterAttribute {
        val prefsAttributes =
            sharedPreferences.getString(
                SHARED_PREFS_FILTERED_ATTRIBUTES,
                gson.toJson(defaultFilterAttribute)
            )

        return gson.fromJson(prefsAttributes, RoomFilterAttribute::class.java)
    }

    fun saveFilteredAttributes(item: RoomFilterAttribute) {
        val values = gson.toJson(item)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_FILTERED_ATTRIBUTES, values)
        editor.apply()
    }

    fun getSearchMovieParameters(): RoomFilterAttribute {
        val prefsAttributes =
            sharedPreferences.getString(
                SHARED_PREFS_SEARCH_MOVIE_ATTRIBUTES,
                gson.toJson(defaultFilterAttribute)
            )

        return gson.fromJson(prefsAttributes, RoomFilterAttribute::class.java)
    }

    fun saveSearchMovieParameters(item: RoomFilterAttribute) {
        val values = gson.toJson(item)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_SEARCH_MOVIE_ATTRIBUTES, values)
        editor.apply()
    }

    fun getCurrentLocation(): RoomCoordinate {
        val prefsCoordinate = sharedPreferences.getString(
            SHARED_PREFS_CURRENT_LOCATION,
            gson.toJson(defaultCoordinate)
        )
        return gson.fromJson(prefsCoordinate, RoomCoordinate::class.java)
    }

    fun saveCurrentLocation(currentLocation: RoomCoordinate) {
        val values = gson.toJson(currentLocation)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_CURRENT_LOCATION, values)
        editor.apply()
    }

    fun getSortWatchMovieList() = sharedPreferences.getBoolean(
        SHARED_PREFS_WATCH_LIST_SORT,
        true
    )

    fun saveSortWatchMovieList(isAsc: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(SHARED_PREFS_WATCH_LIST_SORT, isAsc)
        editor.apply()
    }

    fun getSortMovieList() = sharedPreferences.getBoolean(
        SHARED_PREFS_MOVIES_LIST_SORT,
        true
    )

    fun saveSortMovieList(isAsc: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(SHARED_PREFS_MOVIES_LIST_SORT, isAsc)
        editor.apply()
    }
}