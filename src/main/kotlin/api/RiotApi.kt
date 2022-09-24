package api

import api.responses.League
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.serialization.responseObject
import kotlinx.serialization.json.Json

class RiotApi(private val apiKey: String) {
    fun getLeague(name: String): League {
        val (_, _, result) = Fuel.get("$BASE_URL/tft/league/v1/$name")
            .header(AUTH_HEADER, apiKey)
            .responseObject<League>(json = Json.Default)

        return result.get()
    }

    companion object {
        private val BASE_URL = "https://eun1.api.riotgames.com"
        private val AUTH_HEADER = "X-Riot-Token"
    }
}
