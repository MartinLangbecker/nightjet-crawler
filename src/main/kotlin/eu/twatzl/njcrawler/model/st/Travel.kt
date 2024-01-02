package eu.twatzl.njcrawler.model.st

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Travel(
    val id: String,
    val direction: String,
    val routes: Array<Route>,
    val departureDate: LocalDateTime,
    val originStation: SnalltagetStationSimple,
    val destinationStation: SnalltagetStationSimple,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Travel

        if (id != other.id) return false
        if (direction != other.direction) return false
        if (!routes.contentEquals(other.routes)) return false
        if (departureDate != other.departureDate) return false
        if (originStation != other.originStation) return false
        if (destinationStation != other.destinationStation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + direction.hashCode()
        result = 31 * result + routes.contentHashCode()
        result = 31 * result + departureDate.hashCode()
        result = 31 * result + originStation.hashCode()
        result = 31 * result + destinationStation.hashCode()
        return result
    }
}
