package com.example.flightreviewssubmit.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.flightreviewssubmit.R
import com.example.flightreviewssubmit.data.RateFlightData
import com.example.flightreviewssubmit.ui.recyclerview.adapter.FlightSubmitAdapter
import com.example.flightreviewssubmit.viewmodel.SubmitViewModel

class SubmitFragment : Fragment() {

    private lateinit var submitViewModel: SubmitViewModel

    private lateinit var ratingRecyclerView: RecyclerView
    private lateinit var submitFlightAdapter: FlightSubmitAdapter
    private lateinit var avrRateBar: RatingBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.submit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitViewModel = ViewModelProvider(this).get(SubmitViewModel::class.java)

        ratingRecyclerView = view.findViewById(R.id.rating_recycler_view)
        avrRateBar = view.findViewById(R.id.average_rate_bar)

        initRecyclerView()

        submitViewModel.avrRating.observe(viewLifecycleOwner, Observer {
            avrRateBar.rating = it.toFloat()
            Log.d("Test", "avr:$it")
        })
    }

    private fun initRecyclerView() {
        submitFlightAdapter = FlightSubmitAdapter(
            LayoutInflater.from(context),
            object : FlightSubmitAdapter.IRateActionListener {
                override fun setRating(item: RateFlightData) {
                    submitViewModel.setRating(item)
                    Toast.makeText(context, "${item.rating.value}", Toast.LENGTH_SHORT).show()
                }
            }
        )

        submitViewModel.lstRatings.observe(viewLifecycleOwner, Observer { lstRating ->
            submitFlightAdapter.update(lstRating)
        })

        ratingRecyclerView.adapter = submitFlightAdapter
    }
}