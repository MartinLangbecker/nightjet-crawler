package eu.twatzl.njcrawler.model.st

import kotlinx.serialization.Serializable

@Serializable
data class Option(
    val id: String,
    val description: String,
    val legIds: Array<String>,
    val name: String,
    val productCode: String,
    val passengerFares: Array<PassengerFare>,
    val required: Boolean,
    val price: Float,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Option

        if (id != other.id) return false
        if (description != other.description) return false
        if (!legIds.contentEquals(other.legIds)) return false
        if (name != other.name) return false
        if (productCode != other.productCode) return false
        if (!passengerFares.contentEquals(other.passengerFares)) return false
        if (required != other.required) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + legIds.contentHashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + productCode.hashCode()
        result = 31 * result + passengerFares.contentHashCode()
        result = 31 * result + required.hashCode()
        result = 31 * result + price.hashCode()
        return result
    }
}
