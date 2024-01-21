package eu.twatzl.njcrawler.model.ti

import kotlinx.serialization.Serializable

@Serializable
data class Price(
    val currency: String,
    val amount: Float,
)