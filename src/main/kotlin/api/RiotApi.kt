package api

import api.responses.League
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.serialization.responseObject

class RiotApi(private val apiKey: String) {
    fun getLeague(tier: LeagueTier): League {
        return get("/tft/league/v1/$tier")
    }

    private inline fun <reified T : Any> get(resource: String): T {
        val (_, _, result) = "$BASE_URL$resource"
            .httpGet()
            .header(AUTH_HEADER, apiKey)
            .responseObject<T>()

        return result.get()
    }

    companion object {
        // TODO: consider making the region configurable
        private const val BASE_URL = "https://eun1.api.riotgames.com"
        private const val AUTH_HEADER = "X-Riot-Token"
    }
}
