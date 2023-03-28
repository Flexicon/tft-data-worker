package api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchInfo(
	@SerialName("game_version")
	val gameVersion: String,
	@SerialName("tft_set_number")
	val tftSetNumber: Int,
	@SerialName("game_datetime")
	val gameDatetime: Long,
	@SerialName("game_length")
	val gameLength: Double,
	@SerialName("tft_set_core_name")
	val tftSetCoreName: String,
	@SerialName("queue_id")
	val queueId: Int,
	val participants: List<MatchParticipant>,
	@SerialName("tft_game_type")
	val tftGameType: String
)
