package eu.twatzl.njcrawler.model.ti

import eu.twatzl.njcrawler.model.SimplifiedConnection
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable

@Serializable
data class TiOffer(
    val elements: Array<TiJourney>
) {
    @Serializable
    data class TiJourney(
        val summaries: Array<Summary>,
        val travellers: Array<TiJourneyDetails>
    ) {
        @Serializable
        data class Summary(
            val arrivalLocationName: String,
            val arrivalTime: String,
            val departureLocationName: String,
            val departureTime: String,
        )

        @Serializable
        data class TiJourneyDetails(
            val services: Array<TiServiceDetails>
        ) {
            @Serializable
            data class TiServiceDetails(
                val id: Int,
                val bestOfferId: Int,
                val minPrice: Price,
                val name: String,
                val offers: Array<TiOfferDetails>
            ) {
                @Serializable
                data class TiOfferDetails(
                    val offerId: Int,
                    val name: String,
                    val status: String,
                )

                override fun equals(other: Any?): Boolean {
                    if (this === other) return true
                    if (javaClass != other?.javaClass) return false

                    other as TiServiceDetails

                    if (id != other.id) return false
                    if (bestOfferId != other.bestOfferId) return false
                    if (minPrice != other.minPrice) return false
                    if (name != other.name) return false
                    if (!offers.contentEquals(other.offers)) return false

                    return true
                }

                override fun hashCode(): Int {
                    var result = id
                    result = 31 * result + bestOfferId
                    result = 31 * result + minPrice.hashCode()
                    result = 31 * result + name.hashCode()
                    result = 31 * result + offers.contentHashCode()
                    return result
                }
            }

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as TiJourneyDetails

                return services.contentEquals(other.services)
            }

            override fun hashCode(): Int {
                return services.contentHashCode()
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TiJourney

            if (!summaries.contentEquals(other.summaries)) return false
            if (!travellers.contentEquals(other.travellers)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = summaries.contentHashCode()
            result = 31 * result + travellers.contentHashCode()
            return result
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TiOffer

        return elements.contentEquals(other.elements)
    }

    override fun hashCode(): Int {
        return elements.contentHashCode()
    }

    fun toSimplified(trainId: String, retrievedAt: Instant): SimplifiedConnection {
        val summary = this.elements[0].summaries[0]
        val services = this.elements[0].travellers[0].services // FIXME services seems to be empty - why?

        val seatIds = intArrayOf(
            189, // seat 2nd class
        )
        val couchetteIds = intArrayOf(
            1003, // 4 bed couchette mixed
            1004, // 4 bed couchette women
        )
        val sleeperIds = intArrayOf(
            1019, // single sleeper
            1020, // single sleeper deluxe
        )

        return SimplifiedConnection(
            trainId,
            summary.departureLocationName,
            summary.arrivalLocationName,
            summary.departureTime.toInstant(),
            summary.arrivalTime.toInstant(),
            services.filter { seatIds.contains(it.id) }.minBy { it.minPrice.amount }.minPrice.amount,
            services.filter { couchetteIds.contains(it.id) }.minBy { it.minPrice.amount }.minPrice.amount,
            services.filter { sleeperIds.contains(it.id) }.minBy { it.minPrice.amount }.minPrice.amount,
            retrievedAt,
        )
    }
}