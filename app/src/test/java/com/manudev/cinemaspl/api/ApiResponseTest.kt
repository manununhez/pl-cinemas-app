package com.manudev.cinemaspl.api

import com.manudev.cinemaspl.vo.GeneralResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

private const val MESSAGE_1 = "foo"
private const val MESSAGE_2 = "foo_error_data"
private const val MESSAGE_3 = "blah"
private const val MESSAGE_4 = "Error_blah"
private const val MESSAGE_5 = "foo_error"
private const val MESSAGE_6 = "Errorazo!"

@RunWith(JUnit4::class)
class ApiResponseTest {


    @Test
    fun emptyBody_emptyResponse() {
        val apiResponse = setupEmptyBodyResponse()
        assertThat(apiResponse, isA(ApiEmptyResponse::class.java))
    }


    @Test
    fun nullBody_emptyResponse() {
        val apiResponse = setupNullBodyResponse()
        assertThat(apiResponse, isA(ApiEmptyResponse::class.java))
    }


    @Test
    fun successReturnFromServer_successTrue() {
        val apiResponse = setupSuccessResponse()
        assertThat(apiResponse.body.data, `is`(MESSAGE_1))
    }


    @Test
    fun failReturnFromServer_successFalse_messageEmptyReturnData() {
        val apiResponse = setupSuccessFalseResponseEmptyData()
        assertThat(apiResponse.errorMessage, `is`(MESSAGE_2))
    }


    @Test
    fun failReturnFromServer_successFalse_dataEmptyReturnMessage() {
        val apiResponse = setupSuccessFalseResponseEmptyMessage()
        assertThat(apiResponse.errorMessage, `is`(MESSAGE_5))
    }


    @Test
    fun errorResponse_codeNotBetween200and300_showResponseErrorBody() {
        val (errorMessage) = setupErrorResponseWithCodeNotInRangeAndErrorBody()
        assertThat(errorMessage, `is`(MESSAGE_3))
    }


    @Test
    fun errorResponse_codeNotBetween200and300_responseErrorBodyEmpty_showResponseMessage() {
        val (errorMessage) = setupErrorResponseWithCodeNotInRangeAndWithEmptyErrorBody()
        assertThat(errorMessage, `is`(MESSAGE_6))
    }


    @Test
    fun errorResponse_showingThrowableErrorMessage() {
        val apiResponse = setupErrorResponseWithThrowableErrorMessage()
        assertThat(apiResponse.errorMessage, `is`(MESSAGE_4))

    }

    private fun setupEmptyBodyResponse() = ApiResponse
        .create(Response.success("")) as ApiEmptyResponse

    private fun setupNullBodyResponse() = ApiResponse
        .create(Response.success(null)) as ApiEmptyResponse

    private fun setupSuccessResponse() = ApiResponse
        .create(Response.success(GeneralResponse(true, "", MESSAGE_1))) as ApiSuccessResponse

    private fun setupSuccessFalseResponseEmptyData() = ApiResponse
        .create(Response.success(GeneralResponse(false, "", MESSAGE_2))) as ApiErrorResponse

    private fun setupSuccessFalseResponseEmptyMessage() = ApiResponse
        .create(Response.success(GeneralResponse(false, MESSAGE_5, ""))) as ApiErrorResponse

    private fun setupErrorResponseWithCodeNotInRangeAndErrorBody() = ApiResponse.create<String>(
        Response.error(
            400,
            MESSAGE_3.toResponseBody("application/txt".toMediaType())
        )
    ) as ApiErrorResponse<String>

    private fun setupErrorResponseWithCodeNotInRangeAndWithEmptyErrorBody(): ApiErrorResponse<String> {
        return ApiResponse.create<String>(
            Response.error(
                "".toResponseBody("application/txt".toMediaType()),
                okhttp3.Response.Builder() //
                    .code(400)
                    .message(MESSAGE_6)
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        ) as ApiErrorResponse<String>
    }

    private fun setupErrorResponseWithThrowableErrorMessage() =
        ApiResponse.create<String>(Throwable(MESSAGE_4))

}