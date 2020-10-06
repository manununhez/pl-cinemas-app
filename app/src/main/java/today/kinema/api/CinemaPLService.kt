package today.kinema.api

import androidx.lifecycle.LiveData
import today.kinema.vo.Attribute
import today.kinema.vo.FilterAttribute
import today.kinema.vo.GeneralResponse
import today.kinema.vo.Movies
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import today.kinema.api.ApiResponse


interface CinemaPLService {
    @POST("movies/search")
    fun searchMovies(
        @Body filterAttribute: FilterAttribute
    ): LiveData<ApiResponse<GeneralResponse<List<Movies>>>>

    @GET("attributes")
    fun getAttributes(): LiveData<ApiResponse<GeneralResponse<Attribute>>>

}