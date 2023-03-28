package api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Trait(
	@SerialName("tier_current")
	val tierCurrent: Int,
	val name: String,
	val style: Int,
	@SerialName("tier_total")
	val tierTotal: Int,
	@SerialName("num_units")
	val numUnits: Int
)
