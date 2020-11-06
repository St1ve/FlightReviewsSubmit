package com.example.flightreviewssubmit.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.flightreviewssubmit.R
import com.example.flightreviewssubmit.data.RateFlightData
import com.example.flightreviewssubmit.ui.recyclerview.adapter.FlightSubmitAdapter
import com.example.flightreviewssubmit.viewmodel.SubmitViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.*

class SubmitFragment : Fragment() {

    private lateinit var submitViewModel: SubmitViewModel

    private var appBarLayout: AppBarLayout? = null
    private var exitImageButton: AppCompatImageButton? = null
    private var ratingRecyclerView: RecyclerView? = null
    private var submitFlightAdapter: FlightSubmitAdapter? = null
    private var avrRateBar: RatingBar? = null
    private var foodCheckBox: AppCompatCheckBox? = null
    private var feedbackEditText: AppCompatEditText? = null
    private var mainHeaderSubmitTextView: TextView? = null
    private var infoRaceDateSubmitTextView: TextView? = null
    private var infoDirectionSubmitTextView: TextView? = null
    private var submitButton: AppCompatButton? = null
    private var submitProgressBar: ProgressBar? = null

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

        initUiElements(view)
        initObservers()
        initListeners()
        initRecyclerView()
    }

    private fun initUiElements(view: View) {
        appBarLayout = view.findViewById(R.id.app_bar_layout)
        exitImageButton = view.findViewById(R.id.exit_submit_button)
        mainHeaderSubmitTextView = view.findViewById(R.id.main_header_submit_text_view)
        infoRaceDateSubmitTextView = view.findViewById(R.id.info_race_date_submit_text_view)
        infoDirectionSubmitTextView = view.findViewById(R.id.info_direction_submit_text_view)
        avrRateBar = view.findViewById(R.id.average_rate_bar)
        ratingRecyclerView = view.findViewById(R.id.rating_recycler_view)
        foodCheckBox = view.findViewById(R.id.food_check_box)
        feedbackEditText = view.findViewById(R.id.feedback_edit_text)
        submitButton = view.findViewById(R.id.submit_button)
        submitProgressBar = view.findViewById(R.id.submit_progress_bar)
    }

    private fun initObservers() {
        submitViewModel.isTransactionSucceed.observe(viewLifecycleOwner, Observer {
            if (it.getContentIfNotHandled() == true)
                findNavController().navigate(R.id.action_submitFragment_to_successFragment)
        })

        submitViewModel.avrRating.observe(viewLifecycleOwner, Observer {
            // -1 cause rating range(1,6)
            avrRateBar?.rating = it - 1
        })

        submitViewModel.isFood.observe(viewLifecycleOwner, Observer {
            foodCheckBox?.isChecked = it
        })

        submitViewModel.feedback.observe(viewLifecycleOwner, Observer {
            //Check, if text the same to prevent circle with calls setText().
            if (it != feedbackEditText?.text.toString())
                feedbackEditText?.setText(it)
        })

        submitViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            submitFlightAdapter?.elementsActive = !it
            feedbackEditText?.isEnabled = !it
            foodCheckBox?.isEnabled = !it

            if (it == true) {
                submitButton?.visibility = View.GONE
                submitProgressBar?.visibility = View.VISIBLE
            } else {
                submitButton?.visibility = View.VISIBLE
                submitProgressBar?.visibility = View.GONE
            }
        })

        submitViewModel.flightResult.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { flightData ->
                Toast.makeText(context, "$flightData", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initListeners() {
        appBarLayout?.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (appBarLayout.totalScrollRange + verticalOffset == 0) {
                    mainHeaderSubmitTextView?.visibility = View.GONE
                    infoRaceDateSubmitTextView?.visibility = View.GONE
                    infoDirectionSubmitTextView?.visibility = View.GONE
                    avrRateBar?.visibility = View.GONE
                }
                else {
                    mainHeaderSubmitTextView?.visibility = View.VISIBLE
                    infoRaceDateSubmitTextView?.visibility = View.VISIBLE
                    infoDirectionSubmitTextView?.visibility = View.VISIBLE
                    avrRateBar?.visibility = View.VISIBLE
                }
            }
        )

        exitImageButton?.setOnClickListener {
            showQuitDialog()
        }

        foodCheckBox?.setOnClickListener {
            submitViewModel.setIsFood(foodCheckBox!!.isChecked)
        }

        feedbackEditText?.doOnTextChanged { text, _, _, _ ->
            submitViewModel.setFeedback(text.toString())
        }

        submitButton?.setOnClickListener {
            lifecycleScope.launch {
                submitViewModel.onDataSubmitClick()
            }
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
            submitFlightAdapter?.update(lstRating)
        })

        ratingRecyclerView?.adapter = submitFlightAdapter
    }

    /**
     * Setting up keyboard
     * If user will click on view, keyboard will hide
     */
    @SuppressLint("ClickableViewAccessibility")
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

    override fun onDestroyView() {
        super.onDestroyView()
        setViewItemsToNull()
    }

    private fun setViewItemsToNull() {
        appBarLayout = null
        exitImageButton = null
        ratingRecyclerView = null
        submitFlightAdapter = null
        avrRateBar = null
        foodCheckBox = null
        feedbackEditText = null
        mainHeaderSubmitTextView = null
        infoRaceDateSubmitTextView = null
        infoDirectionSubmitTextView = null
        submitButton = null
        submitProgressBar = null
    }

    private fun Activity.hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(
                this,
                InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun showQuitDialog() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

        val actionCancel = DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() }

        val actionAccept = DialogInterface.OnClickListener { _, _ -> activity?.let {
            finishAffinity(
                it
            )
        } }

        alertDialogBuilder.setMessage(getString(R.string.alert_dialog_quit_message))
        alertDialogBuilder.setTitle(getString(R.string.alert_dialog_quit_title))
        alertDialogBuilder.setNegativeButton("No", actionCancel)
        alertDialogBuilder.setPositiveButton("Yes", actionAccept)
        val dialog: AlertDialog = alertDialogBuilder.create()
        dialog.show()
    }
}