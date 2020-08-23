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

package com.manudev.cinemaspl.api


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.manudev.cinemaspl.repository.NetworkBoundResource
import com.manudev.cinemaspl.util.ApiUtil
import com.manudev.cinemaspl.util.mock
import com.manudev.cinemaspl.vo.GeneralResponse
import com.manudev.cinemaspl.vo.Resource
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class NetworkBoundResourceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var handleCreateCall: () -> LiveData<ApiResponse<GeneralResponse<Foo>>>

    private lateinit var networkBoundResource: NetworkBoundResource<Foo>

    private val observer = mock<Observer<Resource<Foo>>>()


    @Test
    fun successFromNetwork() {

        val networkResult = Foo(1)
        handleCreateCall = { ApiUtil.successCall(GeneralResponse(true, "", networkResult)) }


        networkBoundResource = object : NetworkBoundResource<Foo>() {
            override fun createCall(): LiveData<ApiResponse<GeneralResponse<Foo>>> =
                handleCreateCall()
        }

        networkBoundResource.asLiveData().observeForever(observer)
//        verify(observer).onChanged(Resource.loading(null))
        verify(observer).onChanged(Resource.success(networkResult))

    }

    @Test
    fun successFromNetwork_nullBody_receivedNullData() {
        val handleCreateCall = { ApiUtil.createCall(Response.success<GeneralResponse<String>>(null)) }


        val networkBoundResource = object : NetworkBoundResource<String>() {
            override fun createCall(): LiveData<ApiResponse<GeneralResponse<String>>> =
                handleCreateCall()
        }

        val observer =  mock<Observer<Resource<String>>>()
        networkBoundResource.asLiveData().observeForever(observer)

        verify(observer).onChanged(Resource.success(null))
    }

    @Test
    fun successFromNetwork_emptyData__emptyBody_receivedNullData() {
        val handleCreateCall = { ApiUtil.createCall(Response.success(GeneralResponse(true, "", ""))) }


        val networkBoundResource = object : NetworkBoundResource<String>() {
            override fun createCall(): LiveData<ApiResponse<GeneralResponse<String>>> =
                handleCreateCall()
        }

        val observer =  mock<Observer<Resource<String>>>()
        networkBoundResource.asLiveData().observeForever(observer)
//        verify(observer).onChanged(Resource.loading(null))
        verify(observer).onChanged(Resource.success(""))
    }

    @Test
    fun failureFromNetwork_error() {
        val body = ResponseBody.create("text/html".toMediaType(), "error")
        handleCreateCall = { ApiUtil.createCall(Response.error(500, body)) }


        networkBoundResource = object : NetworkBoundResource<Foo>() {
            override fun createCall(): LiveData<ApiResponse<GeneralResponse<Foo>>> =
                handleCreateCall()
        }


        networkBoundResource.asLiveData().observeForever(observer)

        verify(observer).onChanged(Resource.error("error", null))
    }

    private data class Foo(var value: Int)

}