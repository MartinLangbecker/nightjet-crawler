package eu.twatzl.njcrawler.model.oebb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OebbAccessToken(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("refresh_expires_in") val refreshExpiresIn: Int,
    @SerialName("session_state") val sessionState: String,
    val scope: String,
)