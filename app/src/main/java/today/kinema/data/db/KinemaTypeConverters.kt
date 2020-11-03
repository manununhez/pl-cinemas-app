package today.kinema.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import today.kinema.data.db.model.Cinema
import today.kinema.data.db.model.Movie


object KinemaTypeConverters {
    var gson = Gson()

    /***********
     * Watchlist
     ***********/
    @TypeConverter
    @JvmStatic //fix this: https://stackoverflow.com/questions/51438926/typeconverter-has-private-access-in-typeconverter-error-with-room-in-android
    fun toMovie(data: String?): Movie? {

        val type = object : TypeToken<Movie>() {}.type
        return gson.fromJson<Movie>(data, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromMovie(movies: Movie): String? {
        return gson.toJson(movies)
    }

    /***********
     * Movie
     ***********/
    @TypeConverter
    @JvmStatic //fix this: https://stackoverflow.com/questions/51438926/typeconverter-has-private-access-in-typeconverter-error-with-room-in-android
    fun toCinemaList(data: String): List<Cinema> {

        val type = object : TypeToken<List<Cinema>>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromCinemas(cinemas: List<Cinema>): String {
        return gson.toJson(cinemas)
    }

}