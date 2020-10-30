package com.example.flightreviewssubmit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flightreviewssubmit.data.RateFlightData

class SubmitViewModel : ViewModel() {

    private val _avrRating: MutableLiveData<Float> = MutableLiveData(0.0F)

    val avrRating: LiveData<Float>
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
            val avr: Float = (sum / lstRatings.size)
            _avrRating.postValue(avr)
        }
    }
}