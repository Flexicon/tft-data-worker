package api.responses

import kotlinx.serialization.Serializable

@Serializable
data class LeagueEntry(
    val summonerId: String,
    val summonerName: String,
    val leaguePoints: Int,
    val rank: String,
    val wins: Int,
    val losses: Int,
    val veteran: Boolean,
    val inactive: Boolean,
    val freshBlood: Boolean,
    val hotStreak: Boolean,
)
