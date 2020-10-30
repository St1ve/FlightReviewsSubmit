package com.example.flightreviewssubmit.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.flightreviewssubmit.R
import com.example.flightreviewssubmit.data.RateFlightData
import com.example.flightreviewssubmit.ui.recyclerview.adapter.FlightSubmitAdapter
import com.example.flightreviewssubmit.viewmodel.SubmitViewModel
import com.google.android.material.appbar.AppBarLayout

class SubmitFragment : Fragment() {

    private lateinit var submitViewModel: SubmitViewModel

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var logoToolbarImage: ImageView
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

        appBarLayout = view.findViewById(R.id.app_bar_layout)
        logoToolbarImage = view.findViewById<ImageView>(R.id.logo_toolbar_image)
        ratingRecyclerView = view.findViewById(R.id.rating_recycler_view)
        avrRateBar = view.findViewById(R.id.average_rate_bar)

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener {
                appBarLayout,
                verticalOffset ->
                if (appBarLayout.totalScrollRange + verticalOffset == 0)
                    logoToolbarImage.visibility = View.VISIBLE
                else
                    logoToolbarImage.visibility = View.GONE
        })

        initRecyclerView()

        submitViewModel.avrRating.observe(viewLifecycleOwner, Observer {
            avrRateBar.rating = it
        })
    }

    private fun initRecyclerView() {
        submitFlightAdapter = FlightSubmitAdapter(
            LayoutInflater.from(context),
            object : FlightSubmitAdapter.IRateActionListener {
                override fun setRating(item: RateFlightData) {
                    submitViewModel.setRating(item)
                }
            }
        )

        submitViewModel.lstRatings.observe(viewLifecycleOwner, Observer { lstRating ->
            submitFlightAdapter.update(lstRating)
        })

        ratingRecyclerView.adapter = submitFlightAdapter
    }
}