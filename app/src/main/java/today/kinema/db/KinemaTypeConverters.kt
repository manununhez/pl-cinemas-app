package today.kinema.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import today.kinema.vo.Movies


object KinemaTypeConverters {
    var gson = Gson()

    @TypeConverter
    @JvmStatic //fix this: https://stackoverflow.com/questions/51438926/typeconverter-has-private-access-in-typeconverter-error-with-room-in-android
    fun stringToMovies(data: String?): Movies? {

        val type = object : TypeToken<Movies>() {}.type
        return gson.fromJson<Movies>(data, type)
    }

    @TypeConverter
    @JvmStatic
    fun moviesToString(movies: Movies): String? {
        return gson.toJson(movies)
    }

}