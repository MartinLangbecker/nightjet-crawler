package eu.twatzl.njcrawler.model.ti

import kotlinx.serialization.Serializable

@Serializable
data class TiStation(
    val id: Int,
    val name: String,
    val displayName: String,
    val timezone: String, // usually empty
    val multistation: Boolean,
    val centroidId: Int? // ID of meta station; null if not applicable
)