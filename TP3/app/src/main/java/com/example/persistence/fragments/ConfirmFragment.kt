package com.example.persistence.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.persistence.R
import com.example.persistence.viewModels.SignUpViewModel
import com.example.persistence.Converters
import kotlinx.coroutines.launch

class ConfirmFragment: Fragment() {
    private val viewModel: SignUpViewModel by activityViewModels()
    private lateinit var firstnameText: TextView
    private lateinit var lastnameText: TextView
    private lateinit var birthdateText: TextView
    private lateinit var phoneText: TextView
    private lateinit var mailText: TextView
    private lateinit var loginText: TextView
    private lateinit var passwordText: TextView
    private lateinit var interestsText : TextView
    private var confirmListener: ConfirmSignupInterface? = null


    interface ConfirmSignupInterface{
        suspend fun confirmSignup()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ConfirmSignupInterface) {
            confirmListener = context
        } else {
            throw RuntimeException("$context must implement ConfirmSignupInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.confirm_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parent = requireActivity().findViewById<LinearLayout>(R.id.confirm_fragment)
        Log.d("MODEL", viewModel.login.value.toString())
        firstnameText = parent.findViewById<TextView>(R.id.firstname_confirm)
        firstnameText.text = firstnameText.text.toString()+" : "+viewModel.firstname.value
        lastnameText = parent.findViewById<TextView>(R.id.lastname_confirm)
        lastnameText.text = lastnameText.text.toString()+" : "+viewModel.lastname.value
        birthdateText = parent.findViewById<TextView>(R.id.birthdate_confirm)
        birthdateText.text = birthdateText.text.toString()+" : "+viewModel.birthdateString.value
        phoneText = parent.findViewById<TextView>(R.id.phone_confirm)
        phoneText.text = phoneText.text.toString()+" : "+viewModel.phone.value
        mailText = parent.findViewById<TextView>(R.id.email_confirm)
        mailText.text = mailText.text.toString()+" : "+viewModel.email.value
        loginText = parent.findViewById<TextView>(R.id.login_confirm)
        loginText.text = loginText.text.toString()+" : "+viewModel.login.value
        passwordText = parent.findViewById<TextView>(R.id.password_confirm)
        passwordText.text = passwordText.text.toString()+" : "+viewModel.password.value
        interestsText = parent.findViewById<TextView>(R.id.interests_confirm)
        val interests = viewModel.interests.value
        interestsText.text = interestsText.text.toString()+" : "+Converters.fromInterestsList(interests!!)


        val confirmButton = parent.findViewById<com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton>(R.id.confirm_button)
        confirmButton.setOnClickListener {lifecycleScope.launch {
            confirmListener?.confirmSignup()
        }}
    }
    override fun onDetach() {
        super.onDetach()
        confirmListener = null
    }
}