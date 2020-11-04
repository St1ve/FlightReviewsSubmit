package com.example.flightreviewssubmit.util

import java.lang.UnsupportedOperationException

class RatingRange{
    var value: Int = 1
        set(value) {
            if (value < 1 || value > 7)
                throw UnsupportedOperationException("Rating must be > 1 and < 7")

            field = value
        }
}