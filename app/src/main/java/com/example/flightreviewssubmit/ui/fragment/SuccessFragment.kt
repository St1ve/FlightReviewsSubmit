package com.example.flightreviewssubmit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.flightreviewssubmit.R
import com.example.flightreviewssubmit.viewmodel.SubmitViewModel


class SuccessFragment : Fragment() {

    private lateinit var closeImageButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closeImageButton = view.findViewById(R.id.exit_success_image_button)
        SubmitViewModel.reset()

        closeImageButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}