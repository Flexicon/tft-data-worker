import api.RiotApi

fun main() {
    val apiKey = System.getenv("RIOT_API_KEY") ?: throw IllegalStateException("Missing RIOT_API_KEY")
    val client = RiotApi(apiKey)

    println("TFT data worker")

    client.printLeague("master")
    client.printLeague("challenger")
    client.printLeague("grandmaster")
}

fun RiotApi.printLeague(name: String) {
    val league = getLeague(name)
    println("Master league: \"${league.name}\"")

    league.entries
        .sortedByDescending { it.leaguePoints }
        .take(10)
        .forEach {
            println("\t${it.rank} ${it.leaguePoints} - ${it.summonerName}")
        }
}
