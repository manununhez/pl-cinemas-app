package com.manudev.cinemaspl.api

data class GeneralResponse<T>(val success : Boolean,
                           val message : String,
                           val data : T)
