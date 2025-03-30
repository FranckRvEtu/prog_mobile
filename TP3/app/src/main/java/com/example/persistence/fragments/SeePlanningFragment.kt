package com.example.persistence.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.persistence.R
import com.example.persistence.viewModels.PlanningViewModel

class SeePlanningFragment  : Fragment() {
    private val viewModel: PlanningViewModel by activityViewModels()
    private lateinit var rootView : View
    private lateinit var firstText : TextView
    private lateinit var secondText : TextView
    private lateinit var thirdText : TextView
    private lateinit var fourthText : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.consult_planning_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        val linearLayout = view.findViewById<LinearLayout>(R.id.consult_planning_layout)

        firstText = linearLayout.findViewById(R.id.slot1_text)
        firstText.text = firstText.text.toString()+" : "+viewModel.first.value.toString()
        secondText = linearLayout.findViewById(R.id.slot2_text)
        secondText.text = secondText.text.toString()+" : "+viewModel.second.value.toString()
        thirdText = linearLayout.findViewById(R.id.slot3_text)
        thirdText.text = thirdText.text.toString()+" : "+viewModel.third.value.toString()
        fourthText = linearLayout.findViewById(R.id.slot4_text)
        fourthText.text = fourthText.text.toString()+" : "+viewModel.fourth.value.toString()

    }

}