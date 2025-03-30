package com.example.persistence.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.persistence.R
import com.example.persistence.viewModels.PlanningViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class NewPlanningFragment : Fragment() {
    private lateinit var rootView : View
    private lateinit var firstText : TextInputEditText
    private lateinit var secondText : TextInputEditText
    private lateinit var thirdText : TextInputEditText
    private lateinit var fourthText : TextInputEditText
    private lateinit var submitButton: ExtendedFloatingActionButton
    private val viewModel: PlanningViewModel by activityViewModels()
    private var isTextReady:Boolean = false
    private var createListener : NewPlanningInterface? = null


    interface NewPlanningInterface{
        suspend fun createPlanning()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NewPlanningInterface) {
            createListener = context
        } else {
            throw RuntimeException("$context must implement OnSwitchToLoginInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_planning_layout, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        val linearLayout = view.findViewById<LinearLayout>(R.id.create_planning_layout)


        val firstInput = linearLayout.findViewById<TextInputLayout>(R.id.first_input)
        firstText = firstInput.findViewById(R.id.editText)
        firstText.hint = getString(R.string.first_hour)
        firstText.addTextChangedListener {
            viewModel.setFirst(firstText.text.toString())
            checkTextReady()
        }
        val secondInput = linearLayout.findViewById<TextInputLayout>(R.id.second_input)
        secondText = secondInput.findViewById(R.id.editText)
        secondText.hint = getString(R.string.second_hour)
        secondText.addTextChangedListener {
            viewModel.setSecond(secondText.text.toString())
            checkTextReady()
        }

        val thirdInput = linearLayout.findViewById<TextInputLayout>(R.id.third_input)
        thirdText = thirdInput.findViewById(R.id.editText)
        thirdText.hint = getString(R.string.third_hour)
        thirdText.addTextChangedListener {
            viewModel.setThird(thirdText.text.toString())
            checkTextReady()
        }
        val fourthInput = linearLayout.findViewById<TextInputLayout>(R.id.fourth_input)
        fourthText = fourthInput.findViewById(R.id.editText)
        fourthText.hint = getString(R.string.fourth_hour)
        fourthText.addTextChangedListener {
            viewModel.setFourth(fourthText.text.toString())
            checkTextReady()
        }
        submitButton = linearLayout.findViewById(R.id.create_planning_button)
        submitButton.setOnClickListener{lifecycleScope.launch {
            Log.d("PLANNING", "Create planning clicked")
            createListener?.createPlanning()
        }}

    }

    private fun isButtonReady(){
        submitButton.isEnabled = isTextReady
    }

    private fun checkTextReady(){
        this.isTextReady = firstText.text!!.isNotBlank() &&
                firstText.error == null &&
                secondText.text!!.isNotBlank()&&
                secondText.error == null&&
                thirdText.text!!.isNotBlank()&&
                thirdText.error == null&&
                fourthText.text!!.isNotBlank()&&
                fourthText.error == null

        isButtonReady()
    }

    override fun onDetach() {
        super.onDetach()
        createListener = null
    }
}