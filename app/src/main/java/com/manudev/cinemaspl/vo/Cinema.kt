package com.manudev.cinemaspl.vo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cinema(
    @SerializedName("cinema_id")
    val cinemaId: String,
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("coord_latitude")
    val latitude: String,
    @SerializedName("coord_longitude")
    val longitude: String,
    @SerializedName("logo_url")
    val logoUrl: String,
    @SerializedName("cinema_movie_url")
    val cinemaPageUrl: String
) : Parcelable