package today.kinema.data.db.model

data class FilterAttribute(
    val city: String,
    val date: String,
    val cinema: List<String>,
    val language: List<String>
)