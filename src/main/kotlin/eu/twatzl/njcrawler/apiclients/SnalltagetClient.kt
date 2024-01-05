package eu.twatzl.njcrawler.apiclients

import eu.twatzl.njcrawler.model.st.SearchJourneyRequest
import eu.twatzl.njcrawler.model.st.SnalltagetAccessToken
import eu.twatzl.njcrawler.model.st.SnalltagetJourney
import eu.twatzl.njcrawler.model.st.SnalltagetStationResponse
import eu.twatzl.njcrawler.util.getCurrentTime
import eu.twatzl.njcrawler.util.getFormattedDate
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.Instant

class SnalltagetClient(
    private val httpClient: HttpClient,
) {
    companion object {
        private var token: SnalltagetAccessToken? = null
    }

    private val baseUrl = "https://apiv2.snalltaget.se"
    private val tokenEndpoint = "https://www.snalltaget.se/token/v2"

    suspend fun getOffer(
        fromLocationId: String,
        toLocationId: String,
        travelDate: Instant
    ): SnalltagetJourney? {
        if (token == null || !isTokenValid(token!!)) refreshToken()

        val date = getFormattedDate(travelDate)

        return httpClient.post("$baseUrl/orientation/searchjourney") {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                )
            }
            bearerAuth(token!!.accessToken)
            contentType(ContentType.Application.Json)
            setBody(SearchJourneyRequest(fromLocationId, toLocationId, date))
        }.body<SnalltagetJourney?>() // seems to always return an object even when connections found
    }

    suspend fun getStations(): SnalltagetStationResponse {
        refreshToken()
        return httpClient.get("$baseUrl/navigation/stops") {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                )
            }
            bearerAuth(token!!.accessToken)
            contentType(ContentType.Application.Json)
        }.body()
    }

    private fun isTokenValid(token: SnalltagetAccessToken): Boolean {
        return getCurrentTime().epochSeconds < token.retrievedAt.epochSeconds + token.expiresIn
    }

    private suspend fun refreshToken() {
        token = getToken()
    }

    private suspend fun getToken(): SnalltagetAccessToken {
        return httpClient.get(tokenEndpoint) {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                )
            }
        }.body()
    }
}