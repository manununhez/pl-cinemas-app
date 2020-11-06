package today.kinema.data.db.model

data class Attribute(
    val cinemas: List<String>,
    val cities: List<String>,
    val days: List<Day>,
    val languages: List<String>
)

data class Day(
    val date: String,
    val moviesAvailable: Boolean
)