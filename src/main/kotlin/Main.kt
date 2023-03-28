import api.LeagueTier
import api.Leagues
import api.RiotApi
import api.responses.League
import api.responses.Match
import api.responses.MatchParticipant
import api.responses.Summoner

// TODO: How best to figure out the best builds available in the current league meta:
//
//  ✅ 1. Hit the /tft/league/v1/challenger endpoint to retrieve a list of Challenger league players
//  ✅ 2. Retrieve each player’s PUUID from the the /tft/summoner/v1/summoners/by-name/{summonerName} endpoint
//  ✅ 3. Use each player’s PUUID to retrieve latest match stats by:
//    ✅ 1. Retrieving the latest match UUIDs from /tft/match/v1/matches/by-puuid/{puuid}/ids
//    ✅ 2. Retrieving match data from /tft/match/v1/matches/{matchId}
//  4. Save this data to a DB to be able to analyse and track it over time
fun main() {
    val apiKey = System.getenv("RIOT_API_KEY") ?: throw IllegalStateException("Missing RIOT_API_KEY")
    val client = RiotApi(apiKey)

    println("TFT data worker")

    println("Fetching ${Leagues.MASTER} league...")
    val league = client.getAndPrintLeague(Leagues.MASTER)
    val summoners = league.topEntries().map { client.getSummonerByName(it.summonerName) }

    println("\nWaiting 1s...")
    Thread.sleep(1000)

    println("\nFetching top summoner data...\n")
    val matchesBySummoner = summoners.associateWith { client.getMatchesForSummoner(it) }

    matchesBySummoner.forEach { (summoner, matches) ->
        println(buildString {
            append("${summoner.name}:")

            matches.map { it.participant(summoner.puuid) }
                .filter { it.placement == 1 }
                .ifEmpty { println("No worthy matches recently."); emptyList() }
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

fun RiotApi.getAndPrintLeague(tier: LeagueTier) = getLeague(tier).also { printLeague(it) }

fun RiotApi.getMatchesForSummoner(summoner: Summoner): List<Match> =
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
