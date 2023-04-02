package com.flexicon.tftdata.common.api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamUnit(
    val tier: Int,
    val name: String,
    @SerialName("character_id")
    val characterId: String,
    val itemNames: List<String>,
    val items: List<Int> = emptyList(),
    val rarity: Int,
)
