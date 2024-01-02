package eu.twatzl.njcrawler.data

import eu.twatzl.njcrawler.model.Station
import eu.twatzl.njcrawler.model.TrainConnection
import kotlinx.datetime.LocalDate

val stStockholm = Station("Stockholm C", "740000001")
val stMalmo = Station("Malmö C", "740000003")
val stStorlien = Station("Storlien", "740000025")
val stDuved = Station("Duved", "740000308")
val stBerlin = Station("Berlin Hbf", "800010100")
val stInnsbruck = Station("Innsbruck Hbf", "810000522")

val d300 = TrainConnection("D 300", stBerlin, stMalmo)
val d301 = TrainConnection("D 301", stMalmo, stBerlin)

var d304 = if (today == LocalDate(2024, 3, 2))
    TrainConnection("D 304", stInnsbruck, stStockholm) // March 2, 2024 only
else TrainConnection("D 304", stInnsbruck, stMalmo) // every Saturday until March 30, 2024

var d305 = if (today == LocalDate(2024, 2, 23))
    TrainConnection("D 305", stStockholm, stInnsbruck) // February 23, 2024 only
else TrainConnection("D 305", stMalmo, stInnsbruck) // every Friday until March 29, 2024

var d3900 = if (today <= LocalDate(2024, 2, 7))
    TrainConnection("D 3900", stMalmo, stDuved) // until February 7, 2024
else TrainConnection("D 3900", stMalmo, stStorlien) // starting from February 10, 2024
var d3901 = if ((today <= LocalDate(2024, 2, 11)) || (today == LocalDate(2024, 3, 31)))
    TrainConnection("D 3901", stDuved, stMalmo) // until February 7, 2024
else TrainConnection("D 3901", stStorlien, stMalmo) // starting from February 10, 2024

var d3902 = TrainConnection("D 3902", stMalmo, stDuved) // only Jan 3, Feb 23 and Mar 29
var d3903 = TrainConnection("D 3903", stDuved, stMalmo) // only Apr 1

var d3904 = TrainConnection("D 3904", stMalmo, stDuved) // only Fridays
var d3905 = TrainConnection("D 3905", stDuved, stMalmo) // only Saturdays

val d10300 = TrainConnection("D 10300", stBerlin, stStockholm)
val d10301 = TrainConnection("D 10301", stStockholm, stBerlin)

val allSnalltagets = listOf(d300, d301, d304, d305, d3900, d3901, d3902, d3903, d3904, d3905, d10300, d10301)