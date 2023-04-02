package com.flexicon.tftdata.common.api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchParticipant(
	@SerialName("players_eliminated")
	val playersEliminated: Int,
	@SerialName("time_eliminated")
	val timeEliminated: Double,
	val traits: List<Trait>,
	val level: Int,
	@SerialName("total_damage_to_players")
	val totalDamageToPlayers: Int,
	@SerialName("gold_left")
	val goldLeft: Int,
	val puuid: String,
	val placement: Int,
	val units: List<TeamUnit>,
	val augments: List<String>,
	@SerialName("last_round")
	val lastRound: Int
)
