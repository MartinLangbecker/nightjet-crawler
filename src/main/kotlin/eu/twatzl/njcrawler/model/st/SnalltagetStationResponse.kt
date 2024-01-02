package eu.twatzl.njcrawler.model.st

import kotlinx.serialization.Serializable

@Serializable
data class SnalltagetStationResponse(
    val items: Array<SnalltagetStation>,
    val itemCount: Int
) {
    @Serializable
    data class SnalltagetStation(
        val code: String,
        val shortCode: String? = "",
        val name: String,
        val description: String = "", // currently only used for station "Vemdalen Röjan"
        val synonyms: Array<String>,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SnalltagetStation

            if (code != other.code) return false
            if (shortCode != other.shortCode) return false
            if (name != other.name) return false
            if (!synonyms.contentEquals(other.synonyms)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = code.hashCode()
            result = 31 * result + (shortCode?.hashCode() ?: 0)
            result = 31 * result + name.hashCode()
            result = 31 * result + synonyms.contentHashCode()
            return result
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SnalltagetStationResponse

        if (!items.contentEquals(other.items)) return false
        if (itemCount != other.itemCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = items.contentHashCode()
        result = 31 * result + itemCount
        return result
    }
}
