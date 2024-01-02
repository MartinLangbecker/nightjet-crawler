package eu.twatzl.njcrawler.model.st

import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val id: String,
    val legs: Array<Leg>,
    val bundles: Array<Bundle>,
    val bundleAvailability: BundleAvailability,
    val travelDuration: String, // ISO duration string, e.g. "PT16H10M"
    val lowestPrice: Float? = null,
) {
    @Serializable
    data class BundleAvailability(
        val logicalAvailability: Boolean,
        val availability: Boolean,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Route

        if (id != other.id) return false
        if (!legs.contentEquals(other.legs)) return false
        if (!bundles.contentEquals(other.bundles)) return false
        if (bundleAvailability != other.bundleAvailability) return false
        if (travelDuration != other.travelDuration) return false
        if (lowestPrice != other.lowestPrice) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + legs.contentHashCode()
        result = 31 * result + bundles.contentHashCode()
        result = 31 * result + bundleAvailability.hashCode()
        result = 31 * result + travelDuration.hashCode()
        result = 31 * result + lowestPrice.hashCode()
        return result
    }
}
