package com.manudev.cinemaspl.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.manudev.cinemaspl.api.ApiResponse
import com.manudev.cinemaspl.api.CinemaPLService
import com.manudev.cinemaspl.db.LocalStorage
import com.manudev.cinemaspl.util.AppExecutors
import com.manudev.cinemaspl.vo.Attribute
import com.manudev.cinemaspl.vo.FilterAttribute
import com.manudev.cinemaspl.vo.GeneralResponse
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

    fun loadMovies(filterAttribute: FilterAttribute) =
        object : NetworkBoundResource<List<Movies>, List<Movies>>(appExecutors) {
            override fun saveCallResult(item: List<Movies>) = localStorage.setMovies(item)


            override fun shouldFetch(data: List<Movies>?): Boolean =
                (data == null || data.isEmpty())

            override fun loadFromDb(): LiveData<List<Movies>> =
                localStorage.getMovies(filterAttribute)

            override fun createCall(): LiveData<ApiResponse<GeneralResponse<List<Movies>>>> {
                Log.d(TAG, "createCall")
                setFilteredAttributes(filterAttribute)

                return cinemaPLService.searchMovies(filterAttribute)
            }

        }.asLiveData()

    fun loadAttributes() =
        object : NetworkBoundResource<Attribute, Attribute>(appExecutors) {
            override fun saveCallResult(item: Attribute) = localStorage.setAttributes(item)

            override fun shouldFetch(data: Attribute?): Boolean =
                (data == null)

            override fun loadFromDb(): LiveData<Attribute> = localStorage.getAttributes()

            override fun createCall(): LiveData<ApiResponse<GeneralResponse<Attribute>>> =
                cinemaPLService.getAttributes()
        }.asLiveData()


    fun getFilteredAttributes() = localStorage.getFilteredAttributes()

    fun setFilteredAttributes(item: FilterAttribute) {
        localStorage.setFilteredAttributes(item)
    }


}