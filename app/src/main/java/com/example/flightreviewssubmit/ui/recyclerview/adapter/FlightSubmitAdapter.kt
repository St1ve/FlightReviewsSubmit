package com.example.flightreviewssubmit.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flightreviewssubmit.R
import com.example.flightreviewssubmit.data.RateFlightData
import com.example.flightreviewssubmit.ui.recyclerview.viewholder.RateViewHolder
import com.example.flightreviewssubmit.util.RatingRange

class FlightSubmitAdapter(
    private val inflater: LayoutInflater,
    private val rateListActionListener: IRateActionListener
) : RecyclerView.Adapter<RateViewHolder>() {

    private var lstRatings: List<RateFlightData> = emptyList()

    fun update(newRatings: List<RateFlightData>) {
        lstRatings = newRatings
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return when (viewType) {
            RateFlightData.RATE_CROWDED_VIEW -> {
                val itemView = inflater.inflate(
                    R.layout.item_rate_crowded,
                    parent,
                    false
                )
                RateViewHolder.RateCrowdedViewHolder(itemView)
            }
            RateFlightData.RATE_FLIGHT_VIEW -> {
                val itemView = inflater.inflate(
                    R.layout.item_rate_flight,
                    parent,
                    false
                )
                RateViewHolder.RateFlightViewHolder(itemView)
            }
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val data = lstRatings[position]
        holder.bind(data, rateListActionListener)
    }

    override fun getItemCount(): Int {
        return lstRatings.size
    }

    override fun getItemViewType(position: Int): Int {
        return lstRatings[position].getType()
    }

    interface IRateActionListener {
        fun setRating(item: RateFlightData)
    }
}