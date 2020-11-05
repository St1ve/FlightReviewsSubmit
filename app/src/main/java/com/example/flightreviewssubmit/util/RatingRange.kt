package com.example.flightreviewssubmit.util

import java.lang.UnsupportedOperationException

/**
 *  Wrapper for Int, that make range from 1 to 6
 */
class RatingRange{
    var value: Int = 1
        set(value) {
            if (value < 1 || value > 7)
                throw UnsupportedOperationException("Rating must be > 0 and < 7")

            field = value
        }
}