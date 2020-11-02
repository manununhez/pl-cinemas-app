package today.kinema.data.source

import androidx.lifecycle.LiveData
import today.kinema.data.api.ApiResponse
import today.kinema.data.api.model.Attribute
import today.kinema.vo.FilterAttribute
import today.kinema.data.api.model.GeneralResponse
import today.kinema.data.api.model.Movie

interface RemoteDataSource {
    fun searchMovies(filterAttribute: FilterAttribute): LiveData<ApiResponse<GeneralResponse<List<Movie>>>>
    fun getAttributes(): LiveData<ApiResponse<GeneralResponse<Attribute>>>
}