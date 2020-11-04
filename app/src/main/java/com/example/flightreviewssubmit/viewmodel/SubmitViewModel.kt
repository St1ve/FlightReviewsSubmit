package com.example.flightreviewssubmit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightreviewssubmit.data.FlightData
import com.example.flightreviewssubmit.data.RateFlightData
import com.example.flightreviewssubmit.util.SingleEvent
import kotlinx.coroutines.*

class SubmitViewModel : ViewModel() {

    private val _flightResults: MutableLiveData<SingleEvent<FlightData>> = MutableLiveData()
    val flightResult : LiveData<SingleEvent<FlightData>>
        get() = _flightResults

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _loading

    private val _avrRating: MutableLiveData<Float> = MutableLiveData(0.0F)
    val avrRating: LiveData<Float>
        get() = _avrRating

    /**
     *  true - there was no food during flight
     *  false - there was food during flight
     */
    private val _noFood: MutableLiveData<Boolean> = MutableLiveData(false)
    val isFood: LiveData<Boolean>
        get() = _noFood

    private val _feedback: MutableLiveData<String> = MutableLiveData<String>("")
    val feedback: LiveData<String>
        get() = _feedback

    private val _lstRatings: MutableLiveData<List<RateFlightData>> = MutableLiveData(
        listOf(
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

    fun setIsFood(food: Boolean) {
        _noFood.value = food
    }

    fun setFeedback(feedback: String) {
        _feedback.value = feedback
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

    suspend fun onDataSubmitClick() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.postValue(true)
            val lstRating  = getLstRatingForTransfer()
            _loading.postValue(false)

            _flightResults.postValue(SingleEvent(FlightData(lstRating, feedback.value!!)))
        }
    }

    private suspend fun getLstRatingForTransfer(): ArrayList<RateFlightData?> {
        delay(4000)
        return withContext(Dispatchers.Default) {
            val result = ArrayList<RateFlightData?>()
            _lstRatings.value?.let {
                if (_noFood.value == true)  {
                    for (el in it){
                        if (el.header != "food")
                            result.add(el)
                        else
                            result.add(null)
                    }
                }
                else {
                    result.addAll(it)
                }
            }
            result
        }
    }
}