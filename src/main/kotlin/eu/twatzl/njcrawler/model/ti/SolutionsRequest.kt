package eu.twatzl.njcrawler.model.ti

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class SolutionsRequest(
    val departureLocationId: String, // station ID
    val arrivalLocationId: String, // station ID
    val departureTime: Instant, // YYYY-MM-DDTHH:mm:ss.ZZZ
    val adults: Int = 1,
    val criteria: Criteria = Criteria(),
) {
    @Serializable
    data class Criteria(
        val intercityOnly: Boolean = true,
        val noChanges: Boolean = true,
        val order: String = "DEPARTURE_DATE",
    )
}