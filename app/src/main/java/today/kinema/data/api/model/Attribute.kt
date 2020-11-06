package today.kinema.data.api.model

import com.google.gson.annotations.SerializedName

data class Attribute(
    @SerializedName("cinemas")
    val cinemas: List<String>,
    @SerializedName("cities")
    val cities: List<String>,
    @SerializedName("days")
    val days: List<Day>,
    @SerializedName("languages")
    val languages: List<String>
)

data class Day(
    @SerializedName("date")
    val date: String,
    @SerializedName("movies_available")
    val moviesAvailable: Boolean
)