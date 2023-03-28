package api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchMetadata(
	@SerialName("data_version")
	val dataVersion: String,
	@SerialName("match_id")
	val matchId: String,
	val participants: List<String>
)
