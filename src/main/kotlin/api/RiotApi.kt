package api

import api.responses.League
import api.responses.Match
import api.responses.Summoner
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.serialization.responseObject
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class RiotApi(private val apiKey: String) {
    fun getLeague(tier: LeagueTier) =
        get<League>("$BASE_URL/tft/league/v1/$tier")

    fun getSummonerByName(name: String) =
        get<Summoner>("$BASE_URL/tft/summoner/v1/summoners/by-name/$name")

    fun getMatchIDsByPUUID(puuid: String): List<String> =
        get("$BASE_AGG_URL/tft/match/v1/matches/by-puuid/$puuid/ids", ListSerializer(String.serializer()))

    fun getMatch(id: String) = get<Match>("$BASE_AGG_URL/tft/match/v1/matches/$id")

    private inline fun <reified T : Any> get(url: String): T {
        val (_, _, result) = buildRequest(url).responseObject<T>(json)

        return result.get()
    }

    private inline fun <reified T : Any> get(url: String, loader: DeserializationStrategy<T>): T {
        val (_, _, result) = buildRequest(url).responseObject(loader, json)

        return result.get()
    }

    private fun buildRequest(url: String) = url.httpGet().header(AUTH_HEADER, apiKey)

    companion object {
        // TODO: consider making the region configurable
        private const val BASE_URL = "https://eun1.api.riotgames.com"
        private const val BASE_AGG_URL = "https://europe.api.riotgames.com"
        private const val AUTH_HEADER = "X-Riot-Token"

        private val json = Json { ignoreUnknownKeys = true }
    }
}
