package com.example.flightreviewssubmit.data

data class SubmitState(
    val flightRating: Float,
    val cells: ArrayList<AbsRateFlightCell>,
    val noFood: Boolean,
    val feedback: String,
    val viewState: ViewState,
    val action: Action?
) {
    companion object {
        fun newEmpty(): SubmitState {
            return SubmitState(
                flightRating = 0f,
                cells = ArrayList(),
                noFood = false,
                feedback = "",
                viewState = ViewState.Active,
                action = null
            )
        }
    }
}

sealed class ViewState() {
    object Active : ViewState()
    object Loading : ViewState()
    class Succeed(val data: FlightData) : ViewState()
}

sealed class Action {
    object InitCells : Action()
    class UpdateFoodStatus(val noFood: Boolean) : Action()
    class UpdateFeedBack(val feedback: String) : Action()
    class UpdateFlightRating(val rating: Float) : Action()
    class SetRating(val position: Int, val rating: Float) : Action()
    object OnSubmitClickListener : Action()
}