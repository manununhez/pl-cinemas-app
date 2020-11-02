package today.kinema.data.api.model

import com.google.gson.annotations.SerializedName

data class FilterAttribute(
    @SerializedName("city")
    val city: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("cinema")
    val cinema: List<String>,
    @SerializedName("language")
    val language: List<String>
)