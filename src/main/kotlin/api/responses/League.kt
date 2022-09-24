package api.responses

import kotlinx.serialization.Serializable

@Serializable
data class League(
	val tier: String,
	val leagueId: String,
	val queue: String,
	val name: String,
	val entries: List<LeagueEntry>
)
