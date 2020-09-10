package com.manudev.cinemaspl.vo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    @SerializedName("city")
    val city: String
) : Parcelable

//Only to pass a value navArgs
@Parcelize
data class Locations(
    val locations: List<Location>
) : Parcelable