package eu.twatzl.njcrawler.model.st

import kotlinx.serialization.Serializable

@Serializable
data class Leg(
    val id: String,
    val serviceType: ServiceType,
    val departureStation: SnalltagetStationSimple,
    val serviceScheduleDate: String, // time is always 00:00:00
    val arrivalStation: SnalltagetStationSimple,
    val serviceIdentifier: String,
    val travelInfo: Array<TravelInfo>,
    val serviceName: String // contains train number
) {
    @Serializable
    data class ServiceType(
        val modality: String, // type of train, e.g. "LONG_DISTANCE_TRAINS"
        val name: String, // name of operator, usually "Snälltåget"
        val code: String,
    )

    @Serializable
    data class TravelInfo(
        val title: String,
        val content: String,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Leg

        if (id != other.id) return false
        if (serviceType != other.serviceType) return false
        if (departureStation != other.departureStation) return false
        if (serviceScheduleDate != other.serviceScheduleDate) return false
        if (arrivalStation != other.arrivalStation) return false
        if (serviceIdentifier != other.serviceIdentifier) return false
        if (!travelInfo.contentEquals(other.travelInfo)) return false
        if (serviceName != other.serviceName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + serviceType.hashCode()
        result = 31 * result + departureStation.hashCode()
        result = 31 * result + serviceScheduleDate.hashCode()
        result = 31 * result + arrivalStation.hashCode()
        result = 31 * result + serviceIdentifier.hashCode()
        result = 31 * result + travelInfo.contentHashCode()
        result = 31 * result + serviceName.hashCode()
        return result
    }
}
