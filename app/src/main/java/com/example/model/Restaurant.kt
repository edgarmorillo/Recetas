data class Restaurant(
    val name: String,
    val photo: String,
    val rating: Float,
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)