package eu.twatzl.njcrawler.model.st

import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable

@Serializable
data class SnalltagetJourney(
    val offer: SnalltagetOffer
) {
    @Serializable
    data class SnalltagetOffer(
        val travels: Array<Travel>,
    ) {
        @Serializable
        data class Travel(
            val routes: Array<Route>
        ) {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Travel

                return routes.contentEquals(other.routes)
            }

            override fun hashCode(): Int {
                return routes.contentHashCode()
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SnalltagetOffer

            return travels.contentEquals(other.travels)
        }

        override fun hashCode(): Int {
            return travels.contentHashCode()
        }
    }

    fun toSimplified(trainId: String, retrievedAt: Instant): SimplifiedConnection? {
        val trainNumber = trainId.split(" ").last() // remove train type for API request

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
            // SP = seat, SPSC = seat in compartment, SPPC = private compartment with seats
            // FCS(C) = first class seat (in compartment), FCPC = private compartment with first class seats
            route.bundles.filter { it.productFamilyId == "SP" || it.productFamilyId == "SPSC" || it.productFamilyId == "SPPC" || it.productFamilyId == "FCS" || it.productFamilyId == "FCSC" || it.productFamilyId == "FCPC" }
                .minOfOrNull { it.price },
            // NTB = berth in shared compartment, NTPC(C/E) = private compartment (comfort/extra passenger)
            route.bundles.filter { it.productFamilyId == "NTB" || it.productFamilyId == "NTPC" || it.productFamilyId == "NTPCC" || it.productFamilyId == "NTPCE" }
                .minOfOrNull { it.price },
            null, // sleeper doesn't exist on Snalltaget, only private couchette compartments; comfort compartment could be considered as sleeper since it has already made-up beds and breakfast in dining car is included
            retrievedAt
        )
    }
}
