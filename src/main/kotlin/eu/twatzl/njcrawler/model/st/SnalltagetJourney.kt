package eu.twatzl.njcrawler.model.st

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
}
