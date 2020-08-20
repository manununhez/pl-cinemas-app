package com.manudev.cinemaspl.api

import com.manudev.cinemaspl.util.TestUtil
import com.manudev.cinemaspl.util.TestUtil.createTestResponseError
import com.manudev.cinemaspl.util.TestUtil.createTestResponseError2
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ApiResponseTest {


    @Test
    fun emptyBody_emptyResponse() {
        val apiResponse = ApiResponse
            .create(Response.success("")) as ApiEmptyResponse
        assertThat(apiResponse, isA(ApiEmptyResponse::class.java))
    }

    @Test
    fun nullBody_emptyResponse() {
        val apiResponse = ApiResponse
            .create(Response.success(null)) as ApiEmptyResponse
        assertThat(apiResponse, isA(ApiEmptyResponse::class.java))
    }

    @Test
    fun successReturnFromServer_successTrue() {
        val apiResponse = ApiResponse
            .create(Response.success(TestUtil.createTestResponseSuccess())) as ApiSuccessResponse
        assertThat(apiResponse.body.data, `is`("foo"))
    }

    @Test
    fun failReturnFromServer_successFalse_messageEmptyReturnData() {
        val apiResponse = ApiResponse
            .create(Response.success(createTestResponseError2())) as ApiErrorResponse
        assertThat(apiResponse.errorMessage, `is`("foo_error_data"))
    }

    @Test
    fun failReturnFromServer_successFalse_dataEmptyReturnMessage() {
        val apiResponse = ApiResponse
            .create(Response.success(createTestResponseError())) as ApiErrorResponse
        assertThat(apiResponse.errorMessage, `is`("foo_error"))
    }

    @Test
    fun errorResponse_codeNotBetween200and300_showResponseErrorBody() {
        val errorResponse = Response.error<String>(
            400,
            ResponseBody.create(MediaType.parse("application/txt"), "blah")
        )
        val (errorMessage) = ApiResponse.create<String>(errorResponse) as ApiErrorResponse<String>
        assertThat<String>(errorMessage, `is`("blah"))
    }

    @Test
    fun errorResponse_codeNotBetween200and300_responseErrorBodyEmpty_showResponseMessage() {
        val errorResponse = Response.error<String>(
            ResponseBody.create(MediaType.parse("application/txt"), ""),
            okhttp3.Response.Builder() //
                .code(400)
                .message("Errorazo!")
                .protocol(Protocol.HTTP_1_1)
                .request(Request.Builder().url("http://localhost/").build())
                .build()
        )
        val (errorMessage) = ApiResponse.create<String>(errorResponse) as ApiErrorResponse<String>
        assertThat<String>(errorMessage, `is`("Errorazo!"))
    }

    @Test
    fun errorResponse_showingThrowableErrorMessage() {
        val apiResponse = ApiResponse.create<String>(Throwable("Error_blah"))
        assertThat<String>(apiResponse.errorMessage, `is`("Error_blah"))

    }

}