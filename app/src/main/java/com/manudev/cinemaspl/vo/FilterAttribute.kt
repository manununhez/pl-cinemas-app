package com.manudev.cinemaspl.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterAttribute(
    val city: String,
    val date: String,
    val cinema: List<String>,
    val language: List<String>
): Parcelable