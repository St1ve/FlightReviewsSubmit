package com.example.flightreviewssubmit.data

/**
 * Class for saving data from submit screen
 * @param ratings list of RateFlightData (different rating such as "food", "crew" and so on)
 * @param feedback String, which contains feedback from user
 */
data class FlightData(val ratings: List<RateFlightData?>, val feedback: String) {
    override fun toString(): String {
        var resultString = ""
        for (el in ratings) {
            if (el != null){
                resultString += "\n ${el.name} rating is ${el.rate}"
            } else {
                resultString += "\n null"
            }
        }
        resultString += "\n FeedBack:$feedback"
        return resultString
    }
}