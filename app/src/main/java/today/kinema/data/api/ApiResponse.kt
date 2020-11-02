package today.kinema.data.api

import retrofit2.Response
import today.kinema.data.api.model.GeneralResponse

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<in T> { //For our type of api response, we added 'in' to apply generic T to subTypes only! See GeneralResponse<T> in model, dynamic data type
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error")
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                return if (body == null || body == "") {
                    ApiEmptyResponse()
                } else {
                    val generalResponse = (body as GeneralResponse<T>)
                    val success = generalResponse.success
                    val data = generalResponse.data
                    val message = generalResponse.message

                    if (success) {
                        ApiSuccessResponse(body)
                    } else {
                        val errorMsg = if (message.isEmpty()) {
                            (data as String)
                        } else {
                            message
                        }
                        ApiErrorResponse(errorMsg)
                    }
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }

                ApiErrorResponse(errorMsg ?: "unknown error")
            }
        }

    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()

