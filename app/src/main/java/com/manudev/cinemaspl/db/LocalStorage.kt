package com.manudev.cinemaspl.db

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manudev.cinemaspl.util.AbsentLiveData
import com.manudev.cinemaspl.vo.DateTitle
import com.manudev.cinemaspl.vo.Location
import com.manudev.cinemaspl.vo.Movies
import java.util.*
import javax.inject.Inject

class LocalStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) {
    companion object {
        private const val SHARED_PREFS_DATE = "SelectedDate"
        private const val SHARED_PREFS_CITY = "SelectedCity"
        private const val SHARED_PREFS_MOVIES = "Movies"
        private const val SHARED_PREFS_LOCATIONS = "Locations"
        private const val SHARED_PREFS_DATES = "dates"
        private const val DEFAULT_CITY = "Warszawa"
        private val DEFAULT_CURRENT_DATE = DateTitle.dateFormat(Date())
    }

    fun getSelectedCity() =
        sharedPreferences.getString(SHARED_PREFS_CITY, DEFAULT_CITY)!!

    fun setSelectedCity(city: String) {
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_CITY, city)
        editor.apply()
    }

    fun getSelectedDate() =
        sharedPreferences.getString(SHARED_PREFS_DATE, DEFAULT_CURRENT_DATE)!!

    fun setSelectedDate(date: String) {
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_DATE, date)
        editor.apply()
    }

    fun getMovies(city: String, date: String): LiveData<List<Movies>> {
        val prefsMovies = sharedPreferences.getString(SHARED_PREFS_MOVIES, "")!!
        val prefsCity = getSelectedCity() == city
        val prefsDate = getSelectedDate() == date
        return if (prefsMovies.isNotEmpty() && prefsCity && prefsDate) {
            val type = object : TypeToken<List<Movies>>() {}.type
            val moviesList: List<Movies> = Gson().fromJson(prefsMovies, type)

            val moviesLiveData = MutableLiveData<List<Movies>>()
            moviesLiveData.value = moviesList

            moviesLiveData
        } else {
            AbsentLiveData.create()
        }
    }

    fun setMovies(movies: List<Movies>) {
        val values = Gson().toJson(movies)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_MOVIES, values)
        editor.apply()
    }

    fun getLocations(): LiveData<List<Location>> {
        val prefs: String =
            sharedPreferences.getString(SHARED_PREFS_LOCATIONS, "")!!
        return if (prefs.isNotEmpty()) {
            val type = object : TypeToken<List<Location>>() {}.type
            val locationList: List<Location> = Gson().fromJson(prefs, type)
            val locationLiveData = MutableLiveData<List<Location>>()
            locationLiveData.value = locationList
            locationLiveData
        } else {
            AbsentLiveData.create()
        }
    }

    fun setLocations(item: List<Location>) {
        val values = Gson().toJson(item)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_LOCATIONS, values)
        editor.apply()
    }

    fun getDatesTitle(): LiveData<List<DateTitle>> {
        val prefs = sharedPreferences.getString(SHARED_PREFS_DATES, "")!!
        return if (prefs.isNotEmpty()) {
            val type = object : TypeToken<List<DateTitle>>() {}.type
            val dateList: List<DateTitle> = Gson().fromJson(prefs, type)

            val dateLiveData = MutableLiveData<List<DateTitle>>()
            dateLiveData.value = dateList

            dateLiveData
        } else {
            val dateLiveData = MutableLiveData<List<DateTitle>>()
            dateLiveData.value = listOf()

            AbsentLiveData.create()
        }
    }

    fun setDatesTitle(item: List<DateTitle>) {
        val values = Gson().toJson(item)
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_DATES, values)
        editor.apply()
    }
}