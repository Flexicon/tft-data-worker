package api.responses

import kotlinx.serialization.Serializable

@Serializable
data class Match(
	val metadata: MatchMetadata,
	val info: MatchInfo
)
