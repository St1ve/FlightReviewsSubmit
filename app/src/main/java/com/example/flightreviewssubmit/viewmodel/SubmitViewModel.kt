package com.example.flightreviewssubmit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flightreviewssubmit.util.RatingRange

class SubmitViewModel : ViewModel() {
    private val _rating: MutableLiveData<RatingRange> = MutableLiveData<RatingRange>(RatingRange(0))

    private val rating: LiveData<RatingRange>
        get() = _rating

    fun setRating(rating: RatingRange) {
        _rating.postValue(rating)
    }
}