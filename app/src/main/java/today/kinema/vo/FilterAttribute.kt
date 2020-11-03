package today.kinema.vo

import today.kinema.util.DateUtils
import java.util.*

class FilterAttribute(
    var city: String = DEFAULT_CITY_CODE,
    var date: String = DEFAULT_CURRENT_DATE,
    var cinema: List<String> = listOf(),
    var language: List<String> = listOf()
){
    companion object{
        private const val DEFAULT_CITY_CODE = "Warszawa"
        private val DEFAULT_CURRENT_DATE = DateUtils.dateFormat(Date())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilterAttribute

        if (city != other.city) return false
        if (date != other.date) return false
        if (cinema != other.cinema) return false
        if (language != other.language) return false

        return true
    }

    override fun hashCode(): Int {
        var result = city.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + cinema.hashCode()
        result = 31 * result + language.hashCode()
        return result
    }
}