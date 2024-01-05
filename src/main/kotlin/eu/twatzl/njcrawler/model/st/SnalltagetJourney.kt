package eu.twatzl.njcrawler.model.st

import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable

@Serializable
data class SnalltagetJourney(
    val offer: SnalltagetOffer,
    val messages: Array<String>,
) {
    @Serializable
    data class SnalltagetOffer(
        val productFamilies: Array<ComfortOption>,
        val comfortZones: Array<ComfortOption>,
        val travels: Array<Travel>,
        val passengers: Array<Passenger>,
    ) {
        @Serializable
        data class ComfortOption(
            val id: String,
            val name: String,
            val code: String,
            val description: String = "",
            val travelClass: String = "",
            val flexLevel: String = "",
            val comfortClass: String = "",
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SnalltagetOffer

            if (!productFamilies.contentEquals(other.productFamilies)) return false
            if (!comfortZones.contentEquals(other.comfortZones)) return false
            if (!travels.contentEquals(other.travels)) return false
            if (!passengers.contentEquals(other.passengers)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = productFamilies.contentHashCode()
            result = 31 * result + comfortZones.contentHashCode()
            result = 31 * result + travels.contentHashCode()
            result = 31 * result + passengers.contentHashCode()
            return result
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SnalltagetJourney

        if (offer != other.offer) return false
        if (!messages.contentEquals(other.messages)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = offer.hashCode()
        result = 31 * result + messages.contentHashCode()
        return result
    }

    fun toSimplified(trainId: String, retrievedAt: Instant): SimplifiedConnection? {
        // remove prefix "D " from trainId
        val trainNumber = trainId.substring(2)

        // result should contain direct route of given train number
        if (this.offer.travels[0].routes.none { it.legs.size == 1 && it.legs[0].serviceName == trainNumber }) {
            return null
        }

        val route = this.offer.travels[0].routes.first { it.legs[0].serviceName == trainNumber }
        val leg = route.legs[0]

        return SimplifiedConnection(
            trainId,
            leg.departureStation.name,
            leg.arrivalStation.name,
            leg.departureStation.departureTimestamp!!.toInstant(getTimezone()),
            leg.arrivalStation.arrivalTimestamp!!.toInstant(getTimezone()),
            route.bundles.filter { it.productFamilyId == "SP" }.minOfOrNull { it.price },
            route.bundles.filter { it.productFamilyId == "NTB" }.minOfOrNull { it.price },
            null, // sleeper doesn't exist on Snalltaget, only private compartments
            retrievedAt
        )
    }
}
