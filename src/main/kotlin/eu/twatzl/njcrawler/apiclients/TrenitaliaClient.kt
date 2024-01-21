package eu.twatzl.njcrawler.apiclients

import eu.twatzl.njcrawler.model.ti.Solutions
import eu.twatzl.njcrawler.model.ti.SolutionsRequest
import eu.twatzl.njcrawler.model.ti.TiOffer
import eu.twatzl.njcrawler.model.ti.TiStation
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.Instant

class TrenitaliaClient(
    private val httpClient: HttpClient,
) {
    private val baseUrl = "https://www.lefrecce.it/Channels.Website.BFF.WEB/website"

    /**
     * search direct connections with intercity only for one adult
     */
    suspend fun getSolutions(fromLocationId: String, toLocationId: String, travelDate: Instant): Solutions {
        return httpClient.post("$baseUrl/ticket/solutions") {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                )
            }
            contentType(ContentType.Application.Json)
            setBody(SolutionsRequest(fromLocationId, toLocationId, travelDate))
        }.body<Solutions>()
    }

    suspend fun getOffer(cartId: String, solutionId: String): TiOffer {
        return httpClient.get("$baseUrl/customize/update/travellers") {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                )
            }
            parameter("cartId", cartId)
            parameter("solutionId", solutionId)
            parameter("adults", 1)
        }.body<TiOffer>()
    }

    suspend fun searchStation(name: String, limit: Int = 10): Array<TiStation?> {
        return httpClient.get("$baseUrl/locations/search") {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                )
            }
            parameter("name", name)
            parameter("limit", limit)
        }.body<Array<TiStation?>>()
    }
}