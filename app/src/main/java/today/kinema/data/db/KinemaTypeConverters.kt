package today.kinema.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import today.kinema.vo.Movie


object KinemaTypeConverters {
    var gson = Gson()

    @TypeConverter
    @JvmStatic //fix this: https://stackoverflow.com/questions/51438926/typeconverter-has-private-access-in-typeconverter-error-with-room-in-android
    fun stringToMovies(data: String?): Movie? {

        val type = object : TypeToken<Movie>() {}.type
        return gson.fromJson<Movie>(data, type)
    }

    @TypeConverter
    @JvmStatic
    fun moviesToString(movies: Movie): String? {
        return gson.toJson(movies)
    }

}