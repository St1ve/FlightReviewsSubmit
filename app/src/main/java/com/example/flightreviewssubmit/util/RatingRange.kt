package com.example.flightreviewssubmit.util

import java.lang.UnsupportedOperationException

class RatingRange{
    constructor(rating: Int) {
        if (rating < 0 || rating > 5)
            throw UnsupportedOperationException("Rating must be > 0 and < 5")

        this.rating = rating
    }

    var rating: Int
        set(value) {
            if (rating < 0 || rating > 5)
                throw UnsupportedOperationException("Rating must be > 0 and < 5")

            field = value
        }
}