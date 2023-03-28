package api.responses

import kotlinx.serialization.Serializable

@Serializable
data class Summoner(
	val accountId: String,
	val profileIconId: Int,
	val revisionDate: Long,
	val name: String,
	val puuid: String,
	val id: String,
	val summonerLevel: Int
)
