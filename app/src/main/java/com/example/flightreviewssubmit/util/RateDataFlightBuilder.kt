package com.example.flightreviewssubmit.util

import com.example.flightreviewssubmit.data.FlightData
import com.example.flightreviewssubmit.data.RateFlightCellData
import com.example.flightreviewssubmit.data.RateFlightData
import kotlin.math.roundToInt

object RateDataFlightBuilder {
    private const val DEFAULT_FLIGHT_NAME = "flight"
    private const val DEFAULT_CROWDED_NAME = "crowded"
    private const val DEFAULT_AIRCRAFT_NAME = "aircraft"
    private const val DEFAULT_SEATS_NAME = "seats"
    private const val DEFAULT_CREW_NAME = "crew"
    private const val DEFAULT_FOOD_NAME = "food"

    private const val DEFAULT_NO_FOOD = false
    private const val DEFAULT_RATE = 1
    private const val DEFAULT_FEEDBACK = ""


    fun build(
        rateFlightCellDataList: List<RateFlightCellData>?,
        rateFlight: Float?,
        noFood: Boolean?,
        feedback: String?
    ): FlightData {
        val resultList = ArrayList<RateFlightData?>()
        resultList.add(
            RateFlightData(
                DEFAULT_FLIGHT_NAME,
                rateFlight?.roundToInt()?.plus(1) ?: DEFAULT_RATE
            )
        )
        if (rateFlightCellDataList.isNullOrEmpty()) {
                resultList.addAll(listOf(
                    RateFlightData(DEFAULT_CROWDED_NAME, DEFAULT_RATE),
                    RateFlightData(DEFAULT_AIRCRAFT_NAME, DEFAULT_RATE),
                    RateFlightData(DEFAULT_SEATS_NAME, DEFAULT_RATE),
                    RateFlightData(DEFAULT_CREW_NAME, DEFAULT_RATE)
                )
            )
        } else {
            rateFlightCellDataList.forEach {
                resultList.add(RateFlightData(it.header, it.rating.roundToInt().plus(1)))
            }
        }
        if (noFood == null || rateFlightCellDataList.isNullOrEmpty())
            resultList.add(RateFlightData(DEFAULT_FOOD_NAME, DEFAULT_RATE))
        if (noFood?: DEFAULT_NO_FOOD)
            resultList.add(null)
        return FlightData(resultList, feedback?: DEFAULT_FEEDBACK)
    }
}