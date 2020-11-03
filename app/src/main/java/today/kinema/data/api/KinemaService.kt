package today.kinema.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import today.kinema.data.api.model.Attribute
import today.kinema.data.api.model.FilterAttribute
import today.kinema.data.api.model.GeneralResponse
import today.kinema.data.api.model.Movie


interface KinemaService {
    @POST("movies/search")
    suspend fun searchMovies(
        @Body filterAttribute: FilterAttribute
    ): GeneralResponse<List<Movie>>

    @GET("attributes")
     suspend fun getAttributes(): GeneralResponse<Attribute>

}