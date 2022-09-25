package api

import api.responses.League
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.serialization.responseObject

class RiotApi(private val apiKey: String) {
    fun getLeague(name: String): League {
        return get("/tft/league/v1/$name")
    }

    private inline fun <reified T : Any> get(resource: String): T {
        val (_, _, result) = "$BASE_URL$resource"
            .httpGet()
            .header(AUTH_HEADER, apiKey)
            .responseObject<T>()

        return result.get()
    }

    companion object {
        private val BASE_URL = "https://eun1.api.riotgames.com"
        private val AUTH_HEADER = "X-Riot-Token"
    }
}
