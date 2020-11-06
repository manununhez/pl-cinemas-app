package today.kinema.data.source

import today.kinema.data.api.model.Attribute
import today.kinema.data.api.model.GeneralResponse
import today.kinema.data.api.model.Movie
import today.kinema.vo.FilterAttribute

interface RemoteDataSource {
    suspend fun searchMovies(filterAttribute: FilterAttribute): GeneralResponse<List<Movie>>
    suspend fun getAttributes(filterAttribute: FilterAttribute): GeneralResponse<Attribute>
}