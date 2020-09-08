package com.manudev.cinemaspl.vo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class DayTitle(
    @SerializedName("date")
    val date: String
) : Parcelable {
    companion object {
        private const val datePattern = "yyyy-MM-dd"
        fun dateFormat(): SimpleDateFormat = SimpleDateFormat(datePattern, Locale.getDefault())

        fun currentDate(): String =
            SimpleDateFormat(datePattern, Locale.getDefault()).format(Date())

        fun currentDateWeekName(): String =
            SimpleDateFormat("EEE", Locale.getDefault()).format(Date())
    }
}