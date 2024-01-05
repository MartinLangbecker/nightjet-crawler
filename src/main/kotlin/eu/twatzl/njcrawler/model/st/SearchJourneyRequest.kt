package eu.twatzl.njcrawler.model.st

import kotlinx.serialization.Serializable

@Serializable
data class SearchJourneyRequest(
    val origin: String, // station ID
    val destination: String, // station ID
    val departure: String, // date, YYYY-MM-DD
    val passengers: Array<Passenger> = arrayOf(
        Passenger(
            type = "AD"
        )
    ),
    val travelWithpet: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchJourneyRequest

        if (origin != other.origin) return false
        if (destination != other.destination) return false
        if (departure != other.departure) return false
        if (!passengers.contentEquals(other.passengers)) return false
        if (travelWithpet != other.travelWithpet) return false

        return true
    }

    override fun hashCode(): Int {
        var result = origin.hashCode()
        result = 31 * result + destination.hashCode()
        result = 31 * result + departure.hashCode()
        result = 31 * result + passengers.contentHashCode()
        result = 31 * result + travelWithpet.hashCode()
        return result
    }
}
