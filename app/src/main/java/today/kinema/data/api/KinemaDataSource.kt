package today.kinema.data.api

import androidx.lifecycle.LiveData
import today.kinema.data.api.model.Attribute
import today.kinema.data.api.model.GeneralResponse
import today.kinema.data.api.model.Movie
import today.kinema.data.source.RemoteDataSource
import today.kinema.data.toServerFilterAttribute
import today.kinema.vo.FilterAttribute
import javax.inject.Inject

class KinemaDataSource @Inject constructor(
    private val kinemaService: KinemaService
) : RemoteDataSource {
    override fun searchMovies(filterAttribute: FilterAttribute): LiveData<ApiResponse<GeneralResponse<List<Movie>>>> =
        kinemaService.searchMovies(filterAttribute.toServerFilterAttribute() )


    override fun getAttributes(): LiveData<ApiResponse<GeneralResponse<Attribute>>> =
        kinemaService.getAttributes()

}