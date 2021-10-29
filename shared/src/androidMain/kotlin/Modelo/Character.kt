package Modelo

import kotlinx.serialization.Serializable

@Serializable
class Character(
    val name: String,
    val description: String,
    val thumbnailUrl: String
)