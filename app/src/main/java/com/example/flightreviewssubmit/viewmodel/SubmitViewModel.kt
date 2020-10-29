package com.example.flightreviewssubmit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flightreviewssubmit.data.RateFlightData
import kotlin.math.roundToInt

class SubmitViewModel : ViewModel() {

    private val _avrRating: MutableLiveData<Int> = MutableLiveData(0)

    val avrRating: LiveData<Int>
        get() {
        return _avrRating
    }

    private val _lstRatings: MutableLiveData<List<RateFlightData>> = MutableLiveData(
        listOf<RateFlightData>(
            RateFlightData.RateCrowd(),
            RateFlightData.RateFlight("aircraft"),
            RateFlightData.RateFlight("seats"),
            RateFlightData.RateFlight("crew"),
            RateFlightData.RateFlight("food")
        )
    )

    val lstRatings: LiveData<List<RateFlightData>>
        get() = _lstRatings

    fun setRating(itemRateFlightData: RateFlightData) {
        val positionEl = _lstRatings.value?.indexOf(itemRateFlightData)
        if (positionEl != null)
            _lstRatings.value?.get(positionEl)?.rating = itemRateFlightData.rating
        countAvrRating()
    }

    private fun countAvrRating() {
        val lstRatings = _lstRatings.value
        if (lstRatings != null && lstRatings.isNotEmpty()) {
            var sum = 0.0F
            for(item in lstRatings){
                sum += item.rating.value.toFloat()
            }
            val avr: Int = (sum / lstRatings.size).roundToInt()
            _avrRating.postValue(avr)
        }
    }
}