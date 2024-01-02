package eu.twatzl.njcrawler.model.st

import kotlinx.serialization.Serializable

@Serializable
data class Bundle(
    val id: String,
    val originalPrice: Float,
    val price: Float,
    val productFamilyId: String,
    val requiredItems: Array<RequiredItem>,
    val options: Array<Option>,
    val flexibility: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Bundle

        if (id != other.id) return false
        if (originalPrice != other.originalPrice) return false
        if (price != other.price) return false
        if (productFamilyId != other.productFamilyId) return false
        if (!requiredItems.contentEquals(other.requiredItems)) return false
        if (!options.contentEquals(other.options)) return false
        if (flexibility != other.flexibility) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + originalPrice.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + productFamilyId.hashCode()
        result = 31 * result + requiredItems.contentHashCode()
        result = 31 * result + options.contentHashCode()
        result = 31 * result + flexibility.hashCode()
        return result
    }
}
