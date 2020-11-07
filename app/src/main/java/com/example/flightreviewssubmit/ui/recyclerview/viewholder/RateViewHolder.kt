package com.example.flightreviewssubmit.ui.recyclerview.viewholder

import android.view.View
import android.widget.RatingBar
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flightreviewssubmit.R
import com.example.flightreviewssubmit.data.RateFlightCellData
import com.example.flightreviewssubmit.ui.recyclerview.adapter.FlightSubmitAdapter
import java.lang.UnsupportedOperationException

sealed class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(
        data: RateFlightCellData)

    class RateCrowdedViewHolder(itemView: View, action: FlightSubmitAdapter.IRateActionListener) : RateViewHolder(itemView){
        private val crowdRateBar: AppCompatRatingBar = itemView.findViewById(R.id.crowd_rate_bar)

        init {
            crowdRateBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener {
                    _, rating, _ ->
                action.setRating(adapterPosition, rating)
            }
        }

        override fun bind(
            data: RateFlightCellData
        ) {
            if (data !is RateFlightCellData.RateCrowd)
                throw UnsupportedOperationException("Unsupported data. " +
                        "Data have to be type: RateFlightData.RateFlight")

            crowdRateBar.isEnabled = data.enabled
            crowdRateBar.rating = data.rating
        }
    }

    class RateFlightViewHolder(itemView: View, action: FlightSubmitAdapter.IRateActionListener) : RateViewHolder(itemView) {
        private val headerTextView: AppCompatTextView = itemView.findViewById(R.id.header_text_view)
        private val rateBar: AppCompatRatingBar = itemView.findViewById(R.id.rate_bar)

        init {
            rateBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener {
                    _, rating, _ ->
                action.setRating(adapterPosition, rating)
            }
        }

        override fun bind(
            data: RateFlightCellData
        ) {
            if (data !is RateFlightCellData.RateFlight)
                throw UnsupportedOperationException("Unsupported data." +
                        " Data have to be type: RateFlightData.RateFlight")

            rateBar.isEnabled = data.enabled
            headerTextView.text = itemView.context.getString(
                R.string.rate_flight_header,
                data.header
            )
            rateBar.rating = data.rating
        }
    }
}