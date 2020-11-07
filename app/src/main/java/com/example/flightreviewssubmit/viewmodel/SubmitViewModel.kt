package com.example.flightreviewssubmit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightreviewssubmit.data.FlightData
import com.example.flightreviewssubmit.data.RateFlightCellData
import com.example.flightreviewssubmit.data.RateFlightData
import com.example.flightreviewssubmit.util.RateDataFlightBuilder
import com.example.flightreviewssubmit.util.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubmitViewModel : ViewModel() {

    private val _flightResults: MutableLiveData<SingleEvent<FlightData>> = MutableLiveData()
    val flightResult : LiveData<SingleEvent<FlightData>>
        get() = _flightResults

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _loading

    private val _transactionSucceed: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData(SingleEvent(false))
    val isTransactionSucceed: LiveData<SingleEvent<Boolean>>
        get() = _transactionSucceed

    private val _flightRating: MutableLiveData<Float> = MutableLiveData(0.0F)
    val flightRating: LiveData<Float>
        get() = _flightRating

    /**
     *  true - there was no food during flight
     *  false - there was food during flight
     */
    private val _noFood: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNoFood: LiveData<Boolean>
        get() = _noFood

    private val _feedback: MutableLiveData<String> = MutableLiveData<String>("")
    val feedback: LiveData<String>
        get() = _feedback

    private val _lstRatings: MutableLiveData<List<RateFlightCellData>> = MutableLiveData(
        listOf(
            RateFlightCellData.RateCrowd(rating = 0.0F, enabled = true),
            RateFlightCellData.RateFlight("aircraft", 0.0F, true),
            RateFlightCellData.RateFlight("seats", 0.0F, true),
            RateFlightCellData.RateFlight("crew", 0.0F, true),
            RateFlightCellData.RateFlight("food", 0.0F, true)
        )
    )
    val lstRatings: LiveData<List<RateFlightCellData>>
        get() = _lstRatings

    fun setFlightRating(rating: Float){
        _flightRating.value = rating
    }

    fun setRating(itemRateFlightCellData: RateFlightCellData) {
        val newRating: ArrayList<RateFlightCellData> =  ArrayList()
        for (el in _lstRatings.value!!) {
            if (el.header == itemRateFlightCellData.header)
                newRating.add(itemRateFlightCellData)
            else
                newRating.add(el)
        }
        _lstRatings.value = newRating
    }

    fun setIsFood(food: Boolean) {
        if (food)
            removeFoodRating()
        else
            addFoodRating()
        _noFood.value = food
    }

    private fun addFoodRating(){
        val newRating = ArrayList<RateFlightCellData>()
        newRating.addAll(_lstRatings.value!!)
        newRating.add(RateFlightCellData.RateFlight("food", 0.0F, !_loading.value!!))
        _lstRatings.value = newRating
    }

    private fun removeFoodRating(){
        val newRating = ArrayList<RateFlightCellData>()
        for (el in _lstRatings.value!!) {
            if (el.header != "food")
                newRating.add(el)
        }
        _lstRatings.value = newRating
    }

    fun setFeedback(feedback: String) {
        _feedback.value = feedback
    }

    /**
     * Preparing data for transfer (showing)
     * Set _transactionSucceed to true if data "saving in room" was succeed
     */
    suspend fun onDataSubmitClick() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.postValue(true)
            val result = getLstRatingForTransfer()
            _loading.postValue(false)

            _flightResults.postValue(SingleEvent(result))
            _transactionSucceed.postValue(SingleEvent(true))
        }
    }

    /**
     * Transform _lstRatings.value. If there was no food in flight, then this rating become null,
     * otherwise do not transform list.
     */
    private suspend fun getLstRatingForTransfer(): FlightData {
        delay(4000)
        return withContext(Dispatchers.Default) {
            RateDataFlightBuilder.build(
                _lstRatings.value,
                _flightRating.value,
                _noFood.value,
                _feedback.value
            )
        }
    }
}