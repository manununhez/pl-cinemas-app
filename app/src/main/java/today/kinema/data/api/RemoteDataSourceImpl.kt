package today.kinema.data.api

import today.kinema.data.api.model.Attribute
import today.kinema.data.api.model.GeneralResponse
import today.kinema.data.api.model.Movie
import today.kinema.data.source.RemoteDataSource
import today.kinema.data.toServerFilterAttribute
import today.kinema.vo.FilterAttribute
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val kinemaService: KinemaService
) : RemoteDataSource {
    override suspend fun searchMovies(filterAttribute: FilterAttribute): GeneralResponse<List<Movie>> =
        kinemaService.searchMovies(filterAttribute.toServerFilterAttribute())


    override suspend fun getAttributes(filterAttribute: FilterAttribute): GeneralResponse<Attribute> =
        kinemaService.getAttributes(filterAttribute.toServerFilterAttribute())

}