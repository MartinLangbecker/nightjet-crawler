package eu.twatzl.njcrawler.data

import eu.twatzl.njcrawler.model.Station
import eu.twatzl.njcrawler.model.TrainConnection

val fsMilano = Station("MILANO", "830001650")
val fsSiracusa = Station("Siracusa", "830012349")

val icn1963 = TrainConnection("ICN 1963", fsMilano, fsSiracusa)

val allTrenitalia = listOf(icn1963)