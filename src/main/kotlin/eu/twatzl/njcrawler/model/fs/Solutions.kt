package eu.twatzl.njcrawler.model.fs

import kotlinx.serialization.Serializable

@Serializable
data class Solutions(
    val searchId: String,
    val cartId: String,
    val solutions: Array<SolutionIntermediate>,
) {
    @Serializable
    data class SolutionIntermediate(
        val solution: Solution,
    ) {
        @Serializable
        data class Solution(
            val status: String,
            val trains: Array<Train>,
            val price: Price
        ) {
            @Serializable
            data class Train(
                val name: String // train number
            )

            @Serializable
            data class Price(
                val currency: String,
                val amount: Float,
            )

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Solution

                if (status != other.status) return false
                if (!trains.contentEquals(other.trains)) return false
                if (price != other.price) return false

                return true
            }

            override fun hashCode(): Int {
                var result = status.hashCode()
                result = 31 * result + trains.contentHashCode()
                result = 31 * result + price.hashCode()
                return result
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Solutions

        if (searchId != other.searchId) return false
        if (cartId != other.cartId) return false
        if (!solutions.contentEquals(other.solutions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = searchId.hashCode()
        result = 31 * result + cartId.hashCode()
        result = 31 * result + solutions.contentHashCode()
        return result
    }
}