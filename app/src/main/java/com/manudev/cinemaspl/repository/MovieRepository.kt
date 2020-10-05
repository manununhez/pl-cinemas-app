package com.manudev.cinemaspl.repository

import androidx.lifecycle.LiveData
import com.manudev.cinemaspl.api.ApiResponse
import com.manudev.cinemaspl.api.CinemaPLService
import com.manudev.cinemaspl.db.LocalStorage
import com.manudev.cinemaspl.util.AppExecutors
import com.manudev.cinemaspl.util.LocationUtils.orderCinemasByDistance
import com.manudev.cinemaspl.vo.*
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val cinemaPLService: CinemaPLService,
    private val localStorage: LocalStorage,
    private val appExecutors: AppExecutors
) {

    fun loadMovies(filterAttribute: FilterAttribute) =
        object : NetworkBoundResource<List<Movies>, List<Movies>>(appExecutors) {
            override fun saveCallResult(item: List<Movies>) {
                setFilteredAttributes(filterAttribute)
                //order list based on distance
                localStorage.setMovies(
                    orderCinemasByDistance(
                        getCurrentLocation(),
                        item
                    )
                )
            }

            override fun shouldFetch(data: List<Movies>?): Boolean =
                (data == null || data.isEmpty())

            override fun loadFromDb(): LiveData<List<Movies>> =
                localStorage.getMovies(filterAttribute)

            override fun createCall(): LiveData<ApiResponse<GeneralResponse<List<Movies>>>> =
                cinemaPLService.searchMovies(filterAttribute)

        }.asLiveData()

    fun loadAttributes() =
        object : NetworkBoundResource<Attribute, Attribute>(appExecutors) {
            override fun saveCallResult(item: Attribute) = localStorage.setAttributes(item)

            override fun shouldFetch(data: Attribute?): Boolean =
                true//(data == null) //TODO change when a storage mechanism is added

            override fun loadFromDb(): LiveData<Attribute> = localStorage.getAttributes()

            override fun createCall(): LiveData<ApiResponse<GeneralResponse<Attribute>>> =
                cinemaPLService.getAttributes()
        }.asLiveData()

    fun getFilteredAttributes() = localStorage.getFilteredAttributes()

    fun setFilteredAttributes(item: FilterAttribute) {
        localStorage.setFilteredAttributes(item)
    }

    fun getCurrentLocation() = localStorage.getCurrentLocation()

    fun setCurrentLocation(currentLocation: Coordinate) {
        localStorage.setCurrentLocation(currentLocation)
    }
}