package com.example.flightreviewssubmit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightreviewssubmit.data.*
import com.example.flightreviewssubmit.util.RateDataFlightBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubmitViewModel : ViewModel() {

    companion object{
        private val _state: MutableLiveData<SubmitState> = MutableLiveData(SubmitState.newEmpty())
        fun reset() {
            _state.value = SubmitState.newEmpty()
        }
    }
    val state: LiveData<SubmitState>
        get() = _state

    fun setAction(action: Action) {
        when (action) {
            is Action.InitCells -> {
                _state.value = _state.value!!.copy( cells =
                    arrayListOf(
                        AbsRateFlightCell.RateCrowd(rating = 0.0F, enabled = true),
                        AbsRateFlightCell.RateFlight("aircraft", 0.0F, true),
                        AbsRateFlightCell.RateFlight("seats", 0.0F, true),
                        AbsRateFlightCell.RateFlight("crew", 0.0F, true),
                        AbsRateFlightCell.RateFlight("food", 0.0F, true)
                    )
                )
            }
            is Action.UpdateFoodStatus -> {
                updateFoodStatus(action)
            }
            is Action.UpdateFeedBack -> {
                _state.value = _state.value!!.copy(feedback = action.feedback)
            }
            is Action.UpdateFlightRating -> {
                _state.value = _state.value!!.copy(flightRating = action.rating)
            }
            is Action.SetRating -> {
                setNewCellRating(action.position, action.rating)
            }
            is Action.OnSubmitClickListener -> {
                onSubmitClick()
            }
        }
    }

    private fun updateFoodStatus(action: Action.UpdateFoodStatus) {
        val newCells = ArrayList<AbsRateFlightCell>()
        if (action.noFood) {
            _state.value!!.cells.forEach {
                if (it.header != "food")
                    newCells.add(it)
            }
        } else {
            newCells.addAll(_state.value!!.cells)
            newCells.add(
                AbsRateFlightCell.RateFlight(
                    "food",
                    0f,
                    true
                )
            )
        }
        _state.value = _state.value!!.copy(noFood = action.noFood, cells = newCells)
    }

    private fun getListEnabledElements(enabled: Boolean) : ArrayList<AbsRateFlightCell> {
        val newCells = ArrayList<AbsRateFlightCell>()
        _state.value!!.cells.forEach {
            when (it) {
                is AbsRateFlightCell.RateCrowd -> {
                    newCells.add(
                        AbsRateFlightCell.RateCrowd(
                            rating = it.rating,
                            enabled = enabled
                        )
                    )
                }
                is AbsRateFlightCell.RateFlight -> {
                    newCells.add(
                        AbsRateFlightCell.RateFlight(
                            header = it.header,
                            rating = it.rating,
                            enabled = enabled
                        )
                    )
                }
            }
        }
        return newCells
    }

    private fun setNewCellRating(position: Int, rating: Float) {
        val oldItem = _state.value!!.cells[position]
        if (oldItem is AbsRateFlightCell.RateCrowd)
            _state.value!!.cells[position] = AbsRateFlightCell.RateCrowd(oldItem.header, rating, oldItem.enabled)
        else
            _state.value!!.cells[position] = AbsRateFlightCell.RateFlight(oldItem.header, rating, oldItem.enabled)
    }

    /**
     * Preparing data for transfer (showing)
     * Set _transactionSucceed to true if data "saving in room" was succeed
     */
    private fun onSubmitClick() {
        _state.value = _state.value!!.copy(
            viewState = ViewState.Loading,
            cells = getListEnabledElements(false)
        )
        viewModelScope.launch(Dispatchers.IO) {
            val result = getLstRatingForTransfer()
            _state.postValue(
                _state.value!!.copy(
                    viewState = ViewState.Succeed(result),
                    cells = getListEnabledElements(true)
                )
            )
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
                _state.value!!.cells,
                _state.value!!.flightRating,
                _state.value!!.noFood,
                _state.value!!.feedback
            )
        }
    }
}