package eu.twatzl.njcrawler.app

import eu.twatzl.njcrawler.apiclients.*
import eu.twatzl.njcrawler.data.allEuropeanSleepers
import eu.twatzl.njcrawler.data.allNightjets
import eu.twatzl.njcrawler.data.allSnalltagets
import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.service.ESCrawlerService
import eu.twatzl.njcrawler.service.NightjetCrawlerService
import eu.twatzl.njcrawler.service.PersistenceService
import eu.twatzl.njcrawler.service.StationsResolverService
import eu.twatzl.njcrawler.util.getCurrentTime
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.datetime.LocalDate

// configuration
const val writeCSVPerTrain = true
const val writeOccupationCSV = true
// const val writePricingCSV = true

const val totalTrainsRequested = 21 // must be divisible by 3 for NJ API

suspend fun main() {
    val httpClient = setupHttpClient()
    val persistenceService = PersistenceService()

//    getHafasIdForSingleStation(httpClient)
//    getHafasIdsForStationList(httpClient)
//    printSnalltagetStations(httpClient)

    val njConnections = getDataForNightjets(httpClient)
    val esConnections = getDataForES(httpClient)
    getDataForSt(httpClient)

    // combine connections of different operators
    val allConnections = njConnections + esConnections

    if (writeCSVPerTrain) {
        allConnections.forEach { (train, connections) ->
            persistenceService.writeOffersForTrainToCSV(train, connections)
        }
    }

    if (writeOccupationCSV) {
        persistenceService.writeCombinedOccupationCsv(allConnections)
    }

    httpClient.close()
}

private suspend fun getHafasIdForSingleStation(httpClient: HttpClient) {
    val accessTokenClient = OEBBAccessTokenClient(httpClient)
    val stationClient = OEBBStationClient(httpClient)
    val stationsResolverService = StationsResolverService(
        accessTokenClient,
        stationClient,
    )

    val stationName = "Attnang"
    val result = stationsResolverService.getStationByName(stationName)
    println(result)
}

private suspend fun getHafasIdsForStationList(httpClient: HttpClient) {
    val accessTokenClient = OEBBAccessTokenClient(httpClient)
    val stationClient = OEBBStationClient(httpClient)
    val stationsResolverService = StationsResolverService(
        accessTokenClient,
        stationClient,
    )

    val stationNames = listOf(
        "Attnang",
        "Meidling",
        "Triest",
        "Bruxelles",
        "Bozen"
    )
    val result = stationsResolverService.getStationsBulk(stationNames)
    println(result)
}

private suspend fun printSnalltagetStations(httpClient: HttpClient) {
    val client = SnalltagetClient(httpClient)
    val stationResponse = client.getStations()
    println("Retrieved ${stationResponse.itemCount} Snälltaget stations:")
    stationResponse.items.sortedBy { it.code }.forEach { println("${it.code} ${it.name}") }
}

suspend fun getDataForNightjets(httpClient: HttpClient): Map<TrainConnection, List<SimplifiedConnection>> {
    // define services
    val bookingClient = OEBBNightjetBookingClient(httpClient)
    val nightjetCrawlerService = NightjetCrawlerService(bookingClient)

    return nightjetCrawlerService.requestData(allNightjets, totalTrainsRequested, getCurrentTime())
}

suspend fun getDataForES(httpClient: HttpClient): Map<TrainConnection, List<SimplifiedConnection>> {
    // define services
    val client = EuropeanSleeperClient(httpClient)
    val esCrawlerService = ESCrawlerService(client)

    return esCrawlerService.requestData(allEuropeanSleepers, totalTrainsRequested, getCurrentTime())
}

suspend fun getDataForSt(httpClient: HttpClient) {
    // define services
    val client = SnalltagetClient(httpClient)

    // TODO remove logic after testing
    for (i in 2..9) {
        val travelDate = LocalDate.parse("2024-01-0$i")
        allSnalltagets.forEach {
            println("checking connections on $travelDate for train ${it.trainId}: ${it.fromStation.name} - ${it.toStation.name}...")
            val result = client.getOffer(it.fromStation.id, it.toStation.id, travelDate)

            if (result == null || result.offer.travels[0].routes.isEmpty()) {
                println("no connections found")
            } else {
                println(result.toString())
            }
        }
    }
}

private fun setupHttpClient() = HttpClient(CIO) {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.NONE
        // level = LogLevel.INFO
        // level = LogLevel.ALL // enable for full debug output
        sanitizeHeader { header -> header == HttpHeaders.Authorization || header == "accesstoken" }
    }
    install(ContentNegotiation) {
        json(Json {
            // add more parameters from DefaultJson if needed
            encodeDefaults = true
            ignoreUnknownKeys = true
        })
    }
}



