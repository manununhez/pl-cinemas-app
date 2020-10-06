package today.kinema.vo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Attribute(
    @SerializedName("cinemas")
    val cinemas: List<String>,
    @SerializedName("cities")
    val cities: List<String>,
    @SerializedName("days")
    val days: List<String>,
    @SerializedName("languages")
    val languages: List<String>
): Parcelable