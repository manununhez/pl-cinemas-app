package com.manudev.cinemaspl.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.manudev.cinemaspl.api.ApiResponse
import com.manudev.cinemaspl.api.CinemaPLService
import com.manudev.cinemaspl.db.LocalStorage
import com.manudev.cinemaspl.util.AppExecutors
import com.manudev.cinemaspl.vo.DateTitle
import com.manudev.cinemaspl.vo.GeneralResponse
import com.manudev.cinemaspl.vo.Location
import com.manudev.cinemaspl.vo.Movies
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val cinemaPLService: CinemaPLService,
    private val localStorage: LocalStorage,
    private val appExecutors: AppExecutors
) {

    companion object {
       private val TAG: String = MovieRepository::class.java.simpleName
    }

    fun loadMovies(city: String, date: String) =
        object : NetworkBoundResource<List<Movies>, List<Movies>>(appExecutors) {
            override fun saveCallResult(item: List<Movies>) = localStorage.setMovies(item)

            override fun shouldFetch(data: List<Movies>?): Boolean =
                (data == null || data.isEmpty())

            override fun loadFromDb(): LiveData<List<Movies>> = localStorage.getMovies(city, date)

            override fun createCall(): LiveData<ApiResponse<GeneralResponse<List<Movies>>>> {
                Log.d(TAG, "createCall")
                setSelectedCity(city)
                setSelectedDate(date)
                return cinemaPLService.getMovies(city, date)
            }

        }.asLiveData()

    fun loadLocations() =
        object : NetworkBoundResource<List<Location>, List<Location>>(appExecutors) {
            override fun saveCallResult(item: List<Location>) = localStorage.setLocations(item)

            override fun shouldFetch(data: List<Location>?): Boolean =
                (data == null || data.isEmpty())

            override fun loadFromDb(): LiveData<List<Location>> = localStorage.getLocations()

            override fun createCall(): LiveData<ApiResponse<GeneralResponse<List<Location>>>> =
                cinemaPLService.getLocations()
        }.asLiveData()

    fun loadDates() =
        object : NetworkBoundResource<List<DateTitle>, List<DateTitle>>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<GeneralResponse<List<DateTitle>>>> =
                cinemaPLService.getDates()

            override fun saveCallResult(item: List<DateTitle>) = localStorage.setDatesTitle(item)


            override fun shouldFetch(data: List<DateTitle>?): Boolean =
                (data == null || data.isEmpty())

            override fun loadFromDb(): LiveData<List<DateTitle>> = localStorage.getDatesTitle()

        }.asLiveData()

    fun getSelectedDate() = localStorage.getSelectedDate()

    fun setSelectedDate(date: String) {
        localStorage.setSelectedDate(date)
    }

    fun getSelectedCity() = localStorage.getSelectedCity()

    fun setSelectedCity(city: String) {
        localStorage.setSelectedCity(city)
    }

}