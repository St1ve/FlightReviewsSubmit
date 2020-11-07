package com.example.flightreviewssubmit.data

/**
 * Sealed class which contains 2 types of Rating:
 *  - Rate for crew
 *  - Rate for custom parameter
 *  @param header  name of rating
 *  @param rating  Float. Store grade of rating
 *  @param enabled  Boolean, false - ratingBar is enabled. true - ratingBar is disabled
 */
sealed class RateFlightCellData(
    open val header: String,
    open val rating: Float,
    open val enabled: Boolean) {

    companion object {
        const val RATE_CROWDED_VIEW = 0
        const val RATE_FLIGHT_VIEW = 1
    }

    abstract fun getType(): Int

    class RateCrowd(
        override val header: String = "crowd",
        override val rating: Float,
        override val enabled: Boolean
    ) : RateFlightCellData(header, rating, enabled) {
        override fun getType(): Int {
            return RATE_CROWDED_VIEW
        }

        override fun equals(other: Any?): Boolean {
            if (other is RateCrowd && other.header == this.header && other.rating == this.rating)
                return true
            return false
        }

        override fun hashCode(): Int {
            var result = header.hashCode()
            result = 31 * result + rating.hashCode()
            return result
        }
    }

    data class RateFlight(
        override val header: String,
        override val rating: Float,
        override val enabled: Boolean
    ) : RateFlightCellData(header, rating, enabled) {
        override fun getType(): Int {
            return RATE_FLIGHT_VIEW
        }

        override fun equals(other: Any?): Boolean {
            if (other is RateFlight && other.header == this.header && other.rating == this.rating)
                return true
            return false
        }

        override fun hashCode(): Int {
            var result = header.hashCode()
            result = 31 * result + rating.hashCode()
            return result
        }
    }
}