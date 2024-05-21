package eu.twatzl.njcrawler.data

import eu.twatzl.njcrawler.model.Station
import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.util.getCurrentDay
import kotlinx.datetime.LocalDate

val today = getCurrentDay()

// station data can be retrieved by opening https://www.europeansleeper.eu/
// and inspecting the source code of the departure or arrival station input field

val esBruxelles = Station("Brüssel-Süd", "8800104")
val esPraha = Station("Prag hl.n.", "5457076")

var es452 = TrainConnection("ES 452", esPraha, esBruxelles)
val es453 = TrainConnection("ES 453", esBruxelles, esPraha)

val allEuropeanSleepers = listOf(es452, es453)