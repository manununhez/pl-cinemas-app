package com.manudev.cinemaspl.db

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manudev.cinemaspl.util.AbsentLiveData
import com.manudev.cinemaspl.util.DateUtils
import com.manudev.cinemaspl.vo.Attribute
import com.manudev.cinemaspl.vo.Coordinate
import com.manudev.cinemaspl.vo.FilterAttribute
import com.manudev.cinemaspl.vo.Movies
import java.util.*
import javax.inject.Inject

class LocalStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) {
    companion object {
        private const val SHARED_PREFS_CURRENT_LOCATION = "current_location"
        private const val SHARED_PREFS_MOVIES = "Movies"
        private const val SHARED_PREFS_ATTRIBUTES = "Attributes"
        private const val SHARED_PREFS_SELECTED_ATTRIBUTES = "Selected attributes"
        private const val DEFAULT_CITY = "Warszawa"
        private val DEFAULT_CURRENT_DATE = DateUtils.dateFormat(Date())
    }


    fun getMovies(filterAttribute: FilterAttribute): LiveData<List<Movies>> {
        val prefsMovies = sharedPreferences.getString(SHARED_PREFS_MOVIES, "")!!
        val prefsAttrs = getFilteredAttributes() == filterAttribute
        return if (prefsMovies.isNotEmpty() && prefsAttrs) {
            val type = object : TypeToken<List<Movies>>() {}.type
            val moviesList: List<Movies> = gson.fromJson(prefsMovies, type)

            val moviesLiveData = MutableLiveData<List<Movies>>()
            moviesLiveData.value = moviesList

            moviesLiveData
        } else {
            AbsentLiveData.create()
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
            FilterAttribute(DEFAULT_CITY, DEFAULT_CURRENT_DATE, listOf(), listOf())
        val prefsAttributes =
            sharedPreferences.getString(
                SHARED_PREFS_SELECTED_ATTRIBUTES,
                gson.toJson(filterAttrDefault)
            )

        val type = object : TypeToken<FilterAttribute>() {}.type
        return gson.fromJson(prefsAttributes, type)
    }

    fun setFilteredAttributes(item: FilterAttribute) {
        val values = gson.toJson(item)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_SELECTED_ATTRIBUTES, values)
        editor.apply()
    }

    fun setCurrentLocation(currentLocation: Coordinate) {
        val values = gson.toJson(currentLocation)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_CURRENT_LOCATION, values)
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

}