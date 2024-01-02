package eu.twatzl.njcrawler.model.st

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SnalltagetAccessToken(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Long,
    val retrievedAt: Instant = Clock.System.now(),
)
