package today.kinema.data.api.model

import com.google.gson.annotations.SerializedName

data class Attribute(
    @SerializedName("cinemas")
    val cinemas: List<String>,
    @SerializedName("cities")
    val cities: List<String>,
    @SerializedName("days")
    val days: List<String>,
    @SerializedName("languages")
    val languages: List<String>
)