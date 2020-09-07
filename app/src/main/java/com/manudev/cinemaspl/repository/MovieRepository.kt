package com.manudev.cinemaspl.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manudev.cinemaspl.api.ApiResponse
import com.manudev.cinemaspl.api.CinemaPLService
import com.manudev.cinemaspl.vo.DayTitle
import com.manudev.cinemaspl.vo.GeneralResponse
import com.manudev.cinemaspl.vo.Location
import com.manudev.cinemaspl.vo.Movies
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val cinemaPLService: CinemaPLService,
    private val sharedPreferences: SharedPreferences
) {
    fun loadMovies(city: String, date: String) =
        object : NetworkBoundResource<List<Movies>, List<Movies>>() {
            override fun saveCallResult(item: List<Movies>) {
                val values = Gson().toJson(item)
                val editor = sharedPreferences.edit()
                editor.putString(SHARED_PREFS_CITY, city)
                editor.putString(SHARED_PREFS_DATE, date)
                editor.putString(SHARED_PREFS_MOVIES, values)
                editor.apply()
            }

            override fun shouldFetch(data: List<Movies>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<Movies>> {
                val prefsMovies = sharedPreferences.getString(SHARED_PREFS_MOVIES, "")!!
                val prefsCitySelected = sharedPreferences.getString(SHARED_PREFS_CITY, "")!!
                val prefsDateSelected = sharedPreferences.getString(SHARED_PREFS_DATE, "")!!

                return if (prefsCitySelected == city && prefsDateSelected == date && prefsMovies.isNotEmpty()) {
                    val type = object : TypeToken<List<Movies>>() {}.type
                    val moviesList: List<Movies> = Gson().fromJson(prefsMovies, type)

                    val moviesLiveData = MutableLiveData<List<Movies>>()
                    moviesLiveData.value = moviesList

                    moviesLiveData
                } else {
                    val moviesLiveData = MutableLiveData<List<Movies>>()
                    moviesLiveData.value = listOf()

                    moviesLiveData
                }
            }

            override fun createCall(): LiveData<ApiResponse<GeneralResponse<List<Movies>>>> =
                cinemaPLService.getMovies(city, date)

        }.asLiveData()

    fun loadLocations() = object : NetworkBoundResource<List<Location>, List<Location>>() {
        override fun saveCallResult(item: List<Location>) {
            val values = Gson().toJson(item)
            val editor = sharedPreferences.edit()
            editor.putString(SHARED_PREFS_LOCATIONS, values)
            editor.apply()
        }

        override fun shouldFetch(data: List<Location>?): Boolean {
            return data == null || data.isEmpty()
        }

        override fun loadFromDb(): LiveData<List<Location>> {
            val prefs: String = sharedPreferences.getString(SHARED_PREFS_LOCATIONS, "")!!
            return if (prefs.isNotEmpty()) {
                val type = object : TypeToken<List<Location>>() {}.type
                val locationList: List<Location> = Gson().fromJson(prefs, type)

                val locationLiveData = MutableLiveData<List<Location>>()
                locationLiveData.value = locationList

                locationLiveData
            } else {
                val locationLiveData = MutableLiveData<List<Location>>()
                locationLiveData.value = listOf()

                locationLiveData
            }
        }

        override fun createCall(): LiveData<ApiResponse<GeneralResponse<List<Location>>>> =
            cinemaPLService.getLocations()
    }.asLiveData()

    fun loadDates() = object : NetworkBoundResource<List<DayTitle>, List<DayTitle>>() {
        override fun createCall(): LiveData<ApiResponse<GeneralResponse<List<DayTitle>>>> =
            cinemaPLService.getDates()

        override fun saveCallResult(item: List<DayTitle>) {
            val values = Gson().toJson(item)
            val editor = sharedPreferences.edit()
            editor.putString(SHARED_PREFS_DATES, values)
            editor.apply()
        }

        override fun shouldFetch(data: List<DayTitle>?): Boolean {
            return data == null || data.isEmpty()
        }

        override fun loadFromDb(): LiveData<List<DayTitle>> {
            val prefs = sharedPreferences.getString(SHARED_PREFS_DATES, "")!!
            return if (prefs.isNotEmpty()) {
                val type = object : TypeToken<List<DayTitle>>() {}.type
                val dateList: List<DayTitle> = Gson().fromJson(prefs, type)

                val dateLiveData = MutableLiveData<List<DayTitle>>()
                dateLiveData.value = dateList

                dateLiveData
            } else {
                val dateLiveData = MutableLiveData<List<DayTitle>>()
                dateLiveData.value = listOf()

                dateLiveData
            }
        }
    }.asLiveData()

    fun getSelectedCity(): String {
        val default = "Warszawa"
        return sharedPreferences.getString(SHARED_PREFS_CITY, default)!!
    }

    fun getSelectedDate(): String {
        val default = DayTitle.currentDate()
        return sharedPreferences.getString(SHARED_PREFS_DATE, default)!!
    }

    companion object {
        const val SHARED_PREFS_DATE = "SelectedDate"
        const val SHARED_PREFS_CITY = "SelectedCity"
        const val SHARED_PREFS_MOVIES = "Movies"
        const val SHARED_PREFS_LOCATIONS = "Locations"
        const val SHARED_PREFS_DATES = "dates"
    }
}