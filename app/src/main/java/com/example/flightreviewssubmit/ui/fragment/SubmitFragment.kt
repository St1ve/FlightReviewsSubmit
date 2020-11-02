package com.example.flightreviewssubmit.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RatingBar
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.core.widget.doOnTextChanged
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
    private lateinit var foodCheckBox: AppCompatCheckBox
    private lateinit var feedbackEditText: AppCompatEditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.submit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupKeyBoard(view)

        submitViewModel = ViewModelProvider(this).get(SubmitViewModel::class.java)

        appBarLayout = view.findViewById(R.id.app_bar_layout)
        logoToolbarImage = view.findViewById(R.id.logo_toolbar_image)
        ratingRecyclerView = view.findViewById(R.id.rating_recycler_view)
        avrRateBar = view.findViewById(R.id.average_rate_bar)
        foodCheckBox = view.findViewById(R.id.food_check_box)
        feedbackEditText = view.findViewById(R.id.feedback_edit_text)

        initObservers()
        initListeners()
        initRecyclerView()
    }

    private fun initObservers() {
        submitViewModel.avrRating.observe(viewLifecycleOwner, Observer {
            // -1 cause rating range(1,6)
            avrRateBar.rating = it - 1
        })
        submitViewModel.isFood.observe(viewLifecycleOwner, Observer {
            foodCheckBox.isChecked = it
        })
        submitViewModel.feedback.observe(viewLifecycleOwner, Observer {
            //Check, if text the same to prevent circle with calls setText().
            if (it != feedbackEditText.text.toString())
                feedbackEditText.setText(it)
        })
    }

    private fun initListeners() {
        appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (appBarLayout.totalScrollRange + verticalOffset == 0)
                    logoToolbarImage.visibility = View.VISIBLE
                else
                    logoToolbarImage.visibility = View.GONE
            }
        )

        foodCheckBox.setOnClickListener {
            submitViewModel.setIsFood(foodCheckBox.isChecked)
        }

        feedbackEditText.doOnTextChanged { text, _, _, _ ->
            submitViewModel.setFeedback(text.toString())
        }
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


    /**
     * Setting up keyboard
     * If user will click on view, keyboard will hide
     */
    private fun setupKeyBoard(view: View) {

        if (view !is AppCompatEditText) {
            view.setOnTouchListener { _, _ ->
                activity?.hideSoftKeyboard()
                false
            }
        }

        if (view is ViewGroup) {
            for (el in view) {
                setupKeyBoard(el)
            }
        }
    }

    private fun Activity.hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}