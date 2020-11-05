package com.example.flightreviewssubmit.data

import com.example.flightreviewssubmit.util.RatingRange

/**
 * Sealed class which contains 2 types of Rating:
 *  - Rate for crew
 *  - Rate for custom parameter
 *  @param header  name of rating
 *  @param rating  Int value from 1 to 6
 */
sealed class RateFlightData(open val header: String, open var rating: RatingRange){
    companion object {
        const val RATE_CROWDED_VIEW = 0
        const val RATE_FLIGHT_VIEW = 1
    }

    abstract fun getType(): Int

    class RateCrowd(
        override val header: String = "Crowd",
        override var rating: RatingRange = RatingRange()
    ): RateFlightData(header, rating) {
        override fun getType(): Int {
            return RATE_CROWDED_VIEW
        }
    }

    data class RateFlight(
        override val header: String,
        override var rating: RatingRange = RatingRange()
    ): RateFlightData(header, rating) {
        override fun getType(): Int {
            return RATE_FLIGHT_VIEW
        }
    }
}