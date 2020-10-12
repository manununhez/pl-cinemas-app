package today.kinema.vo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterAttribute(
    @SerializedName("city")
    val city: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("cinema")
    val cinema: List<String>,
    @SerializedName("language")
    val language: List<String>
): Parcelable