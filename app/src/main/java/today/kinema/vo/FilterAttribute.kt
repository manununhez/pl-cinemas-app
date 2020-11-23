package today.kinema.vo

data class FilterAttribute(
    val city: String,
    val date: String,
    val cinema: List<String>,
    val language: List<String>
){


    override fun hashCode(): Int {
        var result = city.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + cinema.hashCode()
        result = 31 * result + language.hashCode()
        return result
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
}