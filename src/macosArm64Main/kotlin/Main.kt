import com.flexicon.tftdata.common.Dummy
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.curl.Curl
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.core.use
import kotlinx.coroutines.runBlocking


// TODO: Refactor old JVM based code to the common source set.
//       Use ktor-client instead of fuel for the HTTP client under the hood.
//       ???
//       Profit 💸
fun main() = runBlocking {
    println("Hello, Native world! 👋")

    HttpClient(Curl) {
        install(ContentNegotiation) {
            json()
        }
    }.use { client ->
        val response: Dummy = client.get("https://tft-suggester-api.nerfthis.xyz/").body()

        println("Status: ${response.msg}")
    }
}
