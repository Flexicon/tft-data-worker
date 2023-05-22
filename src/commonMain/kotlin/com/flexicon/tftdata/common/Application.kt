package com.flexicon.tftdata.common

import com.flexicon.tftdata.common.api.LeagueTier
import com.flexicon.tftdata.common.api.Leagues
import com.flexicon.tftdata.common.api.RiotApi
import com.flexicon.tftdata.common.api.responses.League
import com.flexicon.tftdata.common.api.responses.Match
import com.flexicon.tftdata.common.api.responses.MatchParticipant
import com.flexicon.tftdata.common.api.responses.Summoner
import io.ktor.utils.io.core.use
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import platform.posix.getenv
import platform.posix.sleep

fun runHandling(fn: suspend () -> Unit): Unit = runBlocking {
    runCatching {
        fn()
    }.onFailure {
        println("Error: $it")
        if (isDebug()) it.printStackTrace()
    }
}

fun isDebug() = listOf("true", "1").contains(getenv("DEBUG")?.toKString()?.lowercase())

suspend fun runWorker() {
    println("TFT data worker")
    val apiKey = getenv("RIOT_API_KEY")?.toKString() ?: throw IllegalStateException("Missing RIOT_API_KEY")

    val matchesBySummoner = RiotApi(apiKey).use { client ->
        println("Fetching ${Leagues.MASTER} league...")
        val league = client.getAndPrintLeague(Leagues.MASTER)
        val summoners = league.topEntries().map { client.getSummonerByName(it.summonerName) }

        println("\nWaiting 1s...")
        sleep(1)

        println("\nFetching top summoner data...\n")
        summoners.associateWith { client.getMatchesForSummoner(it) }
    }

    printMatches(matchesBySummoner)
}

suspend fun RiotApi.getAndPrintLeague(tier: LeagueTier) = getLeague(tier).also { printLeague(it) }

suspend fun RiotApi.getMatchesForSummoner(summoner: Summoner): List<Match> =
    getMatchIDsByPUUID(summoner.puuid)
        .take(5)
        .map { getMatch(it) }

fun League.topEntries() = entries
    .sortedByDescending { it.leaguePoints }
    .take(10)

fun Match.participant(puuid: String): MatchParticipant =
    info.participants.find { it.puuid == puuid }
        ?: throw IllegalStateException("Match must include $puuid in participants")

fun printLeague(league: League) {
    println("${league.tier} LEAGUE: \"${league.name}\"")

    league.entries
        .sortedByDescending { it.leaguePoints }
        .take(10)
        .forEach {
            println("\t${it.rank} ${it.leaguePoints} - ${it.summonerName}")
        }
}

fun printMatches(matchesBySummoner: Map<Summoner, List<Match>>) {
    matchesBySummoner.forEach { (summoner, matches) ->
        println(buildString {
            append("${summoner.name}:")

            matches.map { it.participant(summoner.puuid) }
                .filter { it.placement == 1 }
                .ifEmpty { append("No worthy matches recently."); emptyList() }
                .forEachIndexed { i, it ->
                    append("\n\t- Match ${i + 1}")
                    append("\n\t  Level: ${it.level}")
                    append("\n\t  Placement: ${it.placement}")
                    append("\n\t  Traits:")
                    it.traits
                        .filter { it.tierCurrent > 0 }
                        .forEach {
                            append("\n\t\t - ${it.name} x${it.numUnits} (⭐️${it.tierCurrent}/${it.tierTotal})")
                        }

                    append("\n\t  Units:")
                    it.units.forEach {
                        append("\n\t\t - ${it.name} (#${it.characterId}) ⭐️${it.tier}")
                    }
                }
            append("\n")
        })
    }
}
