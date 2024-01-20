package eu.twatzl.njcrawler.model.fs

import kotlinx.serialization.Serializable

@Serializable
data class FsStation(
    val id: Int,
    val name: String,
    val displayName: String,
    val timezone: String, // usually empty
    val multistation: Boolean,
    val centroidId: Int? // ID of meta station; null if not applicable
)