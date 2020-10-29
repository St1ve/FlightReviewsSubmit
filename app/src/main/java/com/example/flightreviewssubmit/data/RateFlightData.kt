package com.example.flightreviewssubmit.data

sealed class RateFlightData{
    companion object {
        const val RATE_CROWDED_VIEW = 0
        const val RATE_FLIGHT_VIEW = 1
    }

    abstract fun getType(): Int

    class RateCrowd(): RateFlightData() {
        override fun getType(): Int {
            return RATE_CROWDED_VIEW
        }
    }

    data class RateFlight(val header: String): RateFlightData() {
        override fun getType(): Int {
            return RATE_FLIGHT_VIEW
        }
    }
}