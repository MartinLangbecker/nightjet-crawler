package eu.twatzl.njcrawler.model.st

import kotlinx.serialization.Serializable

@Serializable
class PassengerFare(
    val passengerId: String,
    val tariffCode: String,
    val price: Float,
)
