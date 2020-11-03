package com.example.flightreviewssubmit.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
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
import kotlin.system.exitProcess

class SubmitFragment : Fragment() {

    private lateinit var submitViewModel: SubmitViewModel

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var exitImageButton: AppCompatImageButton
    private lateinit var ratingRecyclerView: RecyclerView
    private lateinit var submitFlightAdapter: FlightSubmitAdapter
    private lateinit var avrRateBar: RatingBar
    private lateinit var foodCheckBox: AppCompatCheckBox
    private lateinit var feedbackEditText: AppCompatEditText

    private lateinit var mainHeaderSubmitTextView: TextView
    private lateinit var infoRaceDateSubmitTextView: TextView
    private lateinit var infoDirectionSubmitTextView: TextView


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
        exitImageButton = view.findViewById(R.id.exit_submit_button)
        mainHeaderSubmitTextView = view.findViewById(R.id.main_header_submit_text_view)
        infoRaceDateSubmitTextView = view.findViewById(R.id.info_race_date_submit_text_view)
        infoDirectionSubmitTextView = view.findViewById(R.id.info_direction_submit_text_view)
        avrRateBar = view.findViewById(R.id.average_rate_bar)
        ratingRecyclerView = view.findViewById(R.id.rating_recycler_view)
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
                if (appBarLayout.totalScrollRange + verticalOffset == 0) {
                    mainHeaderSubmitTextView.visibility = View.GONE
                    infoRaceDateSubmitTextView.visibility = View.GONE
                    infoDirectionSubmitTextView.visibility = View.GONE
                    avrRateBar.visibility = View.GONE
                }
                else {
                    mainHeaderSubmitTextView.visibility = View.VISIBLE
                    infoRaceDateSubmitTextView.visibility = View.VISIBLE
                    infoDirectionSubmitTextView.visibility = View.VISIBLE
                    avrRateBar.visibility = View.VISIBLE
                }
            }
        )

        exitImageButton.setOnClickListener {
            activity?.finish()
            exitProcess(0)
        }

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