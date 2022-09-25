import api.LeagueTier
import api.Leagues
import api.RiotApi

// TODO: How best to figure out the best builds available in the current league meta:
//
//  1. Hit the /tft/league/v1/challenger endpoint to retrieve a list of Challenger league players
//  2. Retrieve each player’s PUUID from the the /tft/summoner/v1/summoners/by-name/{summonerName} endpoint
//  3. Use each player’s PUUID to retrieve latest match stats by:
//    1. Retrieving the latest match UUIDs from /tft/match/v1/matches/by-puuid/{puuid}/ids
//    2. Retrieving match data from /tft/match/v1/matches/{matchId}
//  4. Save this data to a DB to be able to analyse and track it over time
fun main() {
    val apiKey = System.getenv("RIOT_API_KEY") ?: throw IllegalStateException("Missing RIOT_API_KEY")
    val client = RiotApi(apiKey)

    println("TFT data worker")

    client.printLeague(Leagues.MASTER)
    client.printLeague(Leagues.CHALLENGER)
    client.printLeague(Leagues.GRANDMASTER)
}

fun RiotApi.printLeague(tier: LeagueTier) {
    val league = getLeague(tier)
    println("${league.tier} LEAGUE: \"${league.name}\"")

    league.entries
        .sortedByDescending { it.leaguePoints }
        .take(10)
        .forEach {
            println("\t${it.rank} ${it.leaguePoints} - ${it.summonerName}")
        }
}
