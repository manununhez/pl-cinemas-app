package com.manudev.cinemaspl.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//To pass as a parcelable navArgs
@Parcelize
data class Locations(
    val locations: List<Location>
) : Parcelable