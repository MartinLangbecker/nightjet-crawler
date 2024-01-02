package eu.twatzl.njcrawler.model.st

import kotlinx.serialization.Serializable

@Serializable
data class Passenger(
    val id: String? = null,
    val type: String,
    val travelDocuments: Array<String>? = null // unsure if type is correct
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Passenger

        if (id != other.id) return false
        if (type != other.type) return false
        if (travelDocuments != null) {
            if (other.travelDocuments == null) return false
            if (!travelDocuments.contentEquals(other.travelDocuments)) return false
        } else if (other.travelDocuments != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + type.hashCode()
        result = 31 * result + (travelDocuments?.contentHashCode() ?: 0)
        return result
    }
}