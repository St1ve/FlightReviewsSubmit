package com.example.flightreviewssubmit.data

/**
 * Sealed class which contains 2 types of Rating:
 *  - Rate for crew
 *  - Rate for custom parameter
 *  @param header  name of rating
 *  @param rating  Float. Store grade of rating
 *  @param enabled  Boolean, false - ratingBar is enabled. true - ratingBar is disabled
 */
sealed class AbsRateFlightCell(
    open val header: String,
    open val rating: Float,
    open val enabled: Boolean) {

    class RateCrowd(
        override val header: String = "crowd",
        override val rating: Float,
        override val enabled: Boolean
    ) : AbsRateFlightCell(header, rating, enabled) {
        override fun hashCode(): Int {
            var result = header.hashCode()
            result = 31 * result + rating.hashCode()
            return result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as RateCrowd

            if (header != other.header) return false
            if (rating != other.rating) return false
            if (enabled != other.enabled) return false

            return true
        }
    }

    data class RateFlight(
        override val header: String,
        override val rating: Float,
        override val enabled: Boolean
    ) : AbsRateFlightCell(header, rating, enabled) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as RateFlight

            if (header != other.header) return false
            if (rating != other.rating) return false
            if (enabled != other.enabled) return false

            return true
        }

        override fun hashCode(): Int {
            var result = header.hashCode()
            result = 31 * result + rating.hashCode()
            return result
        }
    }
}