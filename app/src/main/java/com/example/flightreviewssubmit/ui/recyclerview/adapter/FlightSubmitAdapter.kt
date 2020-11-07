package com.example.flightreviewssubmit.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.flightreviewssubmit.R
import com.example.flightreviewssubmit.data.RateFlightCellData
import com.example.flightreviewssubmit.ui.recyclerview.viewholder.RateViewHolder

class FlightSubmitAdapter(
    private val inflater: LayoutInflater,
    private val rateListActionListener: IRateActionListener
) : ListAdapter<RateFlightCellData, RateViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return when (viewType) {
            RateFlightCellData.RATE_CROWDED_VIEW -> {
                val itemView = inflater.inflate(
                    R.layout.item_rate_crowded,
                    parent,
                    false
                )
                RateViewHolder.RateCrowdedViewHolder(itemView, rateListActionListener)
            }
            RateFlightCellData.RATE_FLIGHT_VIEW -> {
                val itemView = inflater.inflate(
                    R.layout.item_rate_flight,
                    parent,
                    false
                )
                RateViewHolder.RateFlightViewHolder(itemView, rateListActionListener)
            }
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    override fun getItemViewType(position: Int): Int = getItem(position).getType()

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil
            .ItemCallback<RateFlightCellData>() {
                override fun areItemsTheSame(
                    oldItem: RateFlightCellData,
                    newItem: RateFlightCellData
                ): Boolean {
                    return (oldItem is RateFlightCellData.RateCrowd &&
                            newItem is RateFlightCellData.RateCrowd ||
                            oldItem is RateFlightCellData.RateFlight &&
                            newItem is RateFlightCellData.RateFlight &&
                            oldItem.header == newItem.header)
                }

                override fun areContentsTheSame(
                    oldItem: RateFlightCellData,
                    newItem: RateFlightCellData
                ): Boolean = oldItem == newItem
        }
    }

    interface IRateActionListener {
        fun setRating(itemPosition: Int, rate: Float)
    }
}