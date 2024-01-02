package eu.twatzl.njcrawler.data

import eu.twatzl.njcrawler.model.Station
import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.util.getCurrentTime
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDateTime

val stStockholm = Station("Stockholm C", "740000001")
val stMalmo = Station("Malmö C", "740000003")
val stBerlin = Station("Berlin Hbf", "800010100")
val stInnsbruck = Station("Innsbruck Hbf", "810000522")

val d300 = TrainConnection("D 300", stBerlin, stMalmo)
val d301 = TrainConnection("D 301", stMalmo, stBerlin)

var d304 = if (getCurrentTime().toLocalDateTime(getTimezone()).date == LocalDate(2024, 3, 2))
    TrainConnection("D 304", stInnsbruck, stStockholm) // March 2, 2024 only
else TrainConnection("D 304", stInnsbruck, stMalmo) // every Saturday until March 30, 2024

var d305 = if (getCurrentTime().toLocalDateTime(getTimezone()).date == LocalDate(2024, 2, 23))
    TrainConnection("D 305", stStockholm, stInnsbruck) // February 23, 2024 only
else TrainConnection("D 305", stMalmo, stInnsbruck) // every Friday until March 29, 2024

val d10300 = TrainConnection("D 10300", stBerlin, stStockholm)
val d10301 = TrainConnection("D 10301", stStockholm, stBerlin)

val allSnalltagets = listOf(d300, d301, d304, d305, d10300, d10301)