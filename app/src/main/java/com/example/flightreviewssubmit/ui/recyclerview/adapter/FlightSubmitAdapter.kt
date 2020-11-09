package com.example.flightreviewssubmit.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.flightreviewssubmit.R
import com.example.flightreviewssubmit.data.AbsRateFlightCell
import com.example.flightreviewssubmit.ui.recyclerview.viewholder.RateViewHolder

class FlightSubmitAdapter(
    private val inflater: LayoutInflater,
    private val rateListActionListener: IRateActionListener
) : ListAdapter<AbsRateFlightCell, RateViewHolder>(DIFF_CALLBACK) {

    private enum class ViewType {
        CUSTOM_RATE,
        RATE
    }

    private val AbsRateFlightCell.viewType: ViewType
        get() = when(this) {
            is AbsRateFlightCell.RateCrowd -> ViewType.CUSTOM_RATE
            is AbsRateFlightCell.RateFlight -> ViewType.RATE
        }

    private val viewTypesValues = ViewType.values()

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeOrdinal: Int): RateViewHolder {
        return when (viewTypesValues[viewTypeOrdinal]) {
            ViewType.CUSTOM_RATE -> {
                val itemView = inflater.inflate(
                    R.layout.item_rate_crowded,
                    parent,
                    false
                )
                RateViewHolder.RateCrowdedViewHolder(itemView, rateListActionListener)
            }
            ViewType.RATE -> {
                val itemView = inflater.inflate(
                    R.layout.item_rate_flight,
                    parent,
                    false
                )
                RateViewHolder.RateFlightViewHolder(itemView, rateListActionListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    override fun getItemViewType(position: Int): Int = getItem(position).viewType.ordinal

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil
            .ItemCallback<AbsRateFlightCell>() {
                override fun areItemsTheSame(
                    oldItem: AbsRateFlightCell,
                    newItem: AbsRateFlightCell
                ): Boolean {
                    return ((oldItem is AbsRateFlightCell.RateCrowd &&
                            newItem is AbsRateFlightCell.RateCrowd ||
                            oldItem is AbsRateFlightCell.RateFlight &&
                            newItem is AbsRateFlightCell.RateFlight) &&
                            oldItem.header == newItem.header)
                }

                override fun areContentsTheSame(
                    oldItem: AbsRateFlightCell,
                    newItem: AbsRateFlightCell
                ): Boolean = oldItem == newItem
        }
    }

    interface IRateActionListener {
        fun setRating(itemPosition: Int, rate: Float)
    }
}