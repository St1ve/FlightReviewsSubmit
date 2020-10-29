package com.example.flightreviewssubmit.util

import java.lang.UnsupportedOperationException

class RatingRange{
    var value: Int = 0
        set(value) {
            if (value < 1 || value > 6)
                throw UnsupportedOperationException("Rating must be > 1 and < 6")

            field = value
        }
}