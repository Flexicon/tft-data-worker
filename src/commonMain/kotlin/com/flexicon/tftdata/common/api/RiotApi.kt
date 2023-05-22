package com.flexicon.tftdata.common.api

import com.flexicon.tftdata.common.api.responses.League
import com.flexicon.tftdata.common.api.responses.Match
import com.flexicon.tftdata.common.api.responses.Summoner
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.curl.Curl
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.encodeURLPath
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.core.Closeable
import kotlinx.serialization.json.Json

class RiotApi(private val apiKey: String): Closeable {
    suspend fun getLeague(tier: LeagueTier): League =
        get("$BASE_URL/tft/league/v1/$tier")

    suspend fun getSummonerByName(name: String): Summoner =
        get("$BASE_URL/tft/summoner/v1/summoners/by-name/$name")

    suspend fun getMatchIDsByPUUID(puuid: String): List<String> =
        get("$BASE_AGG_URL/tft/match/v1/matches/by-puuid/$puuid/ids")

    suspend fun getMatch(id: String): Match =
        get("$BASE_AGG_URL/tft/match/v1/matches/$id")

    private val httpClient by lazy {
        HttpClient(Curl) {
            install(ContentNegotiation) {
                json(serializer)
            }
        }
    }

    private suspend inline fun <reified T : Any> get(url: String): T =
        httpClient.get(url.encodeURLPath()) {
            headers {
                append(AUTH_HEADER, apiKey)
            }
        }.also {
            require(it.status.isSuccess()) { "Bad Riot Api response: ${it.bodyAsText()}" }
        }.body()

    companion object {
        // TODO: consider making the region configurable
        private const val BASE_URL = "https://eun1.api.riotgames.com"
        private const val BASE_AGG_URL = "https://europe.api.riotgames.com"
        private const val AUTH_HEADER = "X-Riot-Token"

        private val serializer = Json { ignoreUnknownKeys = true }
    }

    override fun close() {
        httpClient.close()
    }
}
