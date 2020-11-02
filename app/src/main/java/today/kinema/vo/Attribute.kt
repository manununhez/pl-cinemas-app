package today.kinema.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Attribute(
    val cinemas: List<String>,
    val cities: List<String>,
    val days: List<String>,
    val languages: List<String>
) : Parcelable