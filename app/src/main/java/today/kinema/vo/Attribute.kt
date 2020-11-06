package today.kinema.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Attribute(
    val cinemas: List<String>,
    val cities: List<String>,
    val days: List<Day>,
    val languages: List<String>
) : Parcelable

@Parcelize
data class Day(
    val date: String,
    val moviesAvailable: Boolean
) : Parcelable