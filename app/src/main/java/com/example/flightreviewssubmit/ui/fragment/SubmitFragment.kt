package com.example.flightreviewssubmit.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.flightreviewssubmit.R
import com.example.flightreviewssubmit.data.RateFlightData
import com.example.flightreviewssubmit.ui.recyclerview.adapter.FlightSubmitAdapter
import com.example.flightreviewssubmit.util.RatingRange
import com.example.flightreviewssubmit.viewmodel.SubmitViewModel
import java.lang.UnsupportedOperationException

class SubmitFragment : Fragment() {

    companion object {
        fun newInstance() = SubmitFragment()
    }

    private lateinit var viewModel: SubmitViewModel

    private lateinit var ratingRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.submit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ratingRecyclerView = view.findViewById(R.id.rating_recycler_view)
        initRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SubmitViewModel::class.java)
    }

    private fun initRecyclerView() {
        val ratings = listOf<RateFlightData>(
            RateFlightData.RateCrowd(),
            RateFlightData.RateFlight("How do you rate the aircraft"),
            RateFlightData.RateFlight("How do you rate the seats"),
            RateFlightData.RateFlight("How do you rate the crew"),
            RateFlightData.RateFlight("How do you rate the food")
        )

        val adapter = FlightSubmitAdapter(
            LayoutInflater.from(context),
            ratings,
            object : FlightSubmitAdapter.IRateActionListener {
                override fun setRating(rating: RatingRange) {

                    Toast.makeText(context, "$rating", Toast.LENGTH_SHORT).show()
                }
            }
        )

        ratingRecyclerView.adapter = adapter
    }

}