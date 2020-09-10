package com.manudev.cinemaspl.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manudev.cinemaspl.api.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Response

object ApiUtil {
    fun <T : Any> successCall(data: T) = createCall(Response.success(data))

    private fun <T : Any> createCall(response: Response<T>) = MutableLiveData<ApiResponse<T>>().apply {
        value = ApiResponse.create(response)
    } as LiveData<ApiResponse<T>>

    fun <T : Any> errorCall(errorCode: Int, body: ResponseBody) = createCall(Response.error<T>(errorCode, body))

}
