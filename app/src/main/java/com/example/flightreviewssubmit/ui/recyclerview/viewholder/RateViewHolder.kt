package com.example.flightreviewssubmit.ui.recyclerview.viewholder

import android.view.View
import android.widget.RatingBar
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flightreviewssubmit.R
import com.example.flightreviewssubmit.data.RateFlightData
import com.example.flightreviewssubmit.ui.recyclerview.adapter.FlightSubmitAdapter
import com.example.flightreviewssubmit.util.RatingRange
import java.lang.UnsupportedOperationException

sealed class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(data: RateFlightData, action: FlightSubmitAdapter.IRateActionListener)

    class RateCrowdedViewHolder(itemView: View) : RateViewHolder(itemView){
        private val crowdRateBar: AppCompatRatingBar = itemView.findViewById(R.id.crowd_rate_bar)

        override fun bind(data: RateFlightData, action: FlightSubmitAdapter.IRateActionListener) {
            if (data !is RateFlightData.RateCrowd)
                throw UnsupportedOperationException("Unsupported data. " +
                        "Data have to be type: RateFlightData.RateFlight")

            crowdRateBar.rating = data.rating.value.toFloat()

            crowdRateBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                data.rating.value = rating.toInt()
                action.setRating(data)
            }
        }
    }

    class RateFlightViewHolder(itemView: View) : RateViewHolder(itemView) {
        private val headerTextView: AppCompatTextView = itemView.findViewById(R.id.header_text_view)
        private val rateBar: AppCompatRatingBar = itemView.findViewById(R.id.rate_bar)

        override fun bind(data: RateFlightData, action: FlightSubmitAdapter.IRateActionListener) {

            if (data !is RateFlightData.RateFlight)
                throw UnsupportedOperationException("Unsupported data." +
                        " Data have to be type: RateFlightData.RateFlight")

            headerTextView.text = itemView.context.getString(
                R.string.rate_flight_header,
                data.header
            )

            rateBar.rating = data.rating.value.toFloat()

            rateBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                data.rating.value = rating.toInt()
                action.setRating(data)
            }
        }
    }
}