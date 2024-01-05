package eu.twatzl.njcrawler.model.st

import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val legs: Array<Leg>,
    val bundles: Array<Bundle>
) {
    @Serializable
    data class Leg(
        val departureStation: SnalltagetStationSimple,
        val arrivalStation: SnalltagetStationSimple,
        val serviceName: String // contains train number
    )

    @Serializable
    data class Bundle(
        val price: Float, // in SEK
        val productFamilyId: String,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Route

        if (!legs.contentEquals(other.legs)) return false
        if (!bundles.contentEquals(other.bundles)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = legs.contentHashCode()
        result = 31 * result + bundles.contentHashCode()
        return result
    }
}
