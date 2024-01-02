package eu.twatzl.njcrawler.model.st

import eu.twatzl.njcrawler.model.Station
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class SnalltagetStationSimple(
    val name: String,
    val uicStationCode: String,
    val arrivalTimestamp: LocalDateTime? = null,
    val departureTimestamp: LocalDateTime? = null,
)
