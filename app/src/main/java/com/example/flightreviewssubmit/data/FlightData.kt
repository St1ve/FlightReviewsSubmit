package com.example.flightreviewssubmit.data

data class FlightData(val ratings: List<RateFlightData?>, val feedback: String) {
    override fun toString(): String {
        var resultString = ""
        for (el in ratings) {
            if (el != null){
                resultString += "\n ${el.header} rating is ${el.rating.value}"
            } else {
                resultString += "\n null"
            }
        }
        resultString += "\n FeedBack:$feedback"
        return resultString
    }
}