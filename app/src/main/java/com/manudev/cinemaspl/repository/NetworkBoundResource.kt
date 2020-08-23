/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.manudev.cinemaspl.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manudev.cinemaspl.api.ApiEmptyResponse
import com.manudev.cinemaspl.api.ApiErrorResponse
import com.manudev.cinemaspl.api.ApiResponse
import com.manudev.cinemaspl.api.ApiSuccessResponse
import com.manudev.cinemaspl.vo.GeneralResponse
import com.manudev.cinemaspl.vo.Resource

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType> @MainThread constructor() {

    private val result = MutableLiveData<Resource<ResultType>>()

    init {
        setValue(Resource.loading(null))
        fetchFromNetwork()
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()

        apiResponse.observeForever { response ->
            when (response) {
                is ApiSuccessResponse -> {
                    setValue(Resource.success(processResponse(response)))
                }
                is ApiEmptyResponse -> {
                    setValue(Resource.success(null))
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    setValue(Resource.error(response.errorMessage, null))
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<GeneralResponse<ResultType>>): ResultType =
        response.body.data


    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<GeneralResponse<ResultType>>>

}
