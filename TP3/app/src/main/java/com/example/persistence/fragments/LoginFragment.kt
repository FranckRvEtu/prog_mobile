package com.example.persistence.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.persistence.R
import com.example.persistence.viewModels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private val viewModel : LoginViewModel by activityViewModels()
    private lateinit var loginText: TextInputEditText
    private lateinit var passwordText: TextInputEditText

    interface LoginFragmentInterface {
        fun onSwitchToSignUp()
        suspend fun onLogin()
    }

    private var listener: LoginFragmentInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginFragmentInterface) {
            listener = context
        } else {
            throw RuntimeException("$context doit implémenter LoginFragmentInterface")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var parent = requireActivity().findViewById<ConstraintLayout>(R.id.loginFragment)
        val switchText = parent.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.switchLoginText)
        switchText.setOnClickListener {
            /*parentFragmentManager.beginTransaction()
                .replace(R.id.login_fragment_container, SignUpFragment())
                .addToBackStack(null)
                .commit()*/
            listener?.onSwitchToSignUp()
        }
        val submitButton = parent.findViewById<com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton>(R.id.submitLoginButton)
        val loginInput = parent.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.login_input)
        val passwordInput = parent.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.password_input)
        loginInput.hint = getString(R.string.login_hint)
        passwordInput.hint = getString(R.string.password_hint)
        loginText = loginInput.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editText)
        passwordText = passwordInput.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editText)



        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val isReady = loginText.text!!.isNotBlank() && passwordText.text!!.isNotBlank()
                submitButton.isEnabled = isReady
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        loginText.addTextChangedListener(textWatcher)
        passwordText.addTextChangedListener(textWatcher)

        submitButton.setOnClickListener {lifecycleScope.launch {
            Log.d("LOGIN", "Clicked")
            viewModel.setLogin(loginText.text.toString())
            viewModel.setPassword(passwordText.text.toString())
            listener?.onLogin()
        }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}