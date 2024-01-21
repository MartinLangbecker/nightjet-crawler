package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.apiclients.TrenitaliaClient
import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.model.Station
import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.util.getCurrentDay
import eu.twatzl.njcrawler.util.getCurrentTime
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus

class TiCrawlerService(private val client: TrenitaliaClient) {
    suspend fun requestData(
        trains: List<TrainConnection>,
        totalTrainsRequested: Int,
        startTime: Instant = getCurrentDay().atStartOfDayIn(getTimezone()),
    ): Map<TrainConnection, List<SimplifiedConnection>> {
        val offers = mutableMapOf<TrainConnection, List<SimplifiedConnection>>()

        trains.forEachIndexed { idx, train ->
            val trainId = train.trainId
            val fromStation = train.fromStation
            val toStation = train.toStation
            println("starting requesting offers for $trainId")

            offers[train] =
                requestOffers(trainId, fromStation, toStation, startTime, totalTrainsRequested)
                    .distinctBy { it.departure }
                    .sortedBy { it.departure }

            println("(${idx + 1}/${trains.size}) Train $trainId done ✔")
        }

        return offers
    }

    private suspend fun requestOffers(
        trainId: String,
        fromStation: Station,
        toStation: Station,
        startTime: Instant,
        totalTrainsRequested: Int
    ): List<SimplifiedConnection> {
        val offers = mutableListOf<SimplifiedConnection>()
        var time = startTime

        repeat(totalTrainsRequested) { _ ->
            offers.addAll(callTiApiSafe(trainId, fromStation, toStation, time))
            time = time.plus(1, DateTimeUnit.DAY, getTimezone())
        }

        return offers
    }

    private suspend fun callTiApiSafe(
        trainId: String,
        fromStation: Station,
        toStation: Station,
        startTime: Instant,
        maxRequests: Int = 3,
    ): List<SimplifiedConnection> {
        val offers = mutableListOf<SimplifiedConnection>()

        val result = runCatching {
            val solutions = client.getSolutions(
                fromStation.id,
                toStation.id,
                startTime,
            )
            val trainNumber = trainId.split(" ").last()
            val solutionId = solutions.solutions.first { it.solution.trains[0].name == trainNumber }.solution.id

            client.getOffer(solutions.cartId, solutionId).toSimplified(trainId, getCurrentTime())
        }

        result.onSuccess {
            offers.add(it)
            println("$trainId: found connection on $startTime")
        }

        result.onFailure {
            println("$trainId ${fromStation.name} - ${toStation.name}: error fetching connections from $startTime")
            println(it.cause)
            println(it.message)
            it.stackTrace.forEach { element -> println(element) }

            // on failure, we add error offers to indicate in the final csv that a timeout occurred
            repeat(maxRequests) { count ->
                val errorTime = startTime.plus(count, DateTimeUnit.DAY, getTimezone())
                offers.add(
                    SimplifiedConnection.errorOffer(
                        trainId,
                        fromStation,
                        toStation,
                        errorTime
                    )
                )
            }
        }

        return offers
    }
}
