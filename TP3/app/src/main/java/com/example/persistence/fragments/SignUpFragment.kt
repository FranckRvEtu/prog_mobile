package com.example.persistence.fragments

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.NOT_FOCUSABLE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.persistence.R
import com.example.persistence.viewModels.SignUpViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class SignUpFragment : Fragment() {
    private var isTextReady:Boolean = false
    private var isChipsReady:Boolean = false
    private lateinit var submitButton: ExtendedFloatingActionButton
    private val viewModel: SignUpViewModel by activityViewModels()
    private lateinit var loginText: TextInputEditText
    private lateinit var passwordText: TextInputEditText
    private lateinit var firstnameText: TextInputEditText
    private lateinit var lastnameText: TextInputEditText
    private lateinit var birthdateInputEdit: TextInputEditText
    private lateinit var phoneText: TextInputEditText
    private lateinit var mailText: TextInputEditText
    private var signupListener: SignupFragmentInterface? = null
    private lateinit var date: LocalDate
    private val selectedChips = mutableListOf<String>()


    interface SignupFragmentInterface{
        fun onSwitchToLogin()
        suspend fun signup()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SignupFragmentInterface) {
            signupListener = context
        } else {
            throw RuntimeException("$context must implement OnSwitchToLoginInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parent = requireActivity().findViewById<ConstraintLayout>(R.id.signUpFragment)
        val linearLayout = parent.findViewById<LinearLayout>(R.id.signUpLinear)

        val loginInput = linearLayout.findViewById<TextInputLayout>(R.id.login_input)
        loginInput.hint = getString(R.string.login_hint)
        this.loginText = loginInput.findViewById<TextInputEditText>(R.id.editText)
        viewModel.login.observe(viewLifecycleOwner) { login ->
            if (!login.equals(loginText.text.toString())){
                loginText.setText(login)
                Log.d("TEST",login)
            }
        }
        loginText.addTextChangedListener{login ->
            //viewModel.setLogin(login.toString())
            viewModel.validateLogin(login.toString())
            checkTextReady()
        }
        viewModel.loginError.observe(viewLifecycleOwner){
            loginText.error = it
        }

        val passwordInput = linearLayout.findViewById<TextInputLayout>(R.id.password_input)
        passwordInput.hint = getString(R.string.password_hint)
        this.passwordText = passwordInput.findViewById<TextInputEditText>(R.id.editText)
        passwordText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        viewModel.password.observe(viewLifecycleOwner) {
            if(!it.equals(passwordText.text.toString())){
                passwordText.setText(it)

            }
        }
        passwordText.addTextChangedListener{password ->
            //viewModel.setPassword(password.toString())
            viewModel.validatePassword(password.toString())
            checkTextReady()
        }
        viewModel.passwordError.observe(viewLifecycleOwner){
            passwordText.error = it
        }

        val firstnameInput = linearLayout.findViewById<TextInputLayout>(R.id.firstname_input)
        firstnameInput.hint = getString(R.string.firstname_hint)
        this.firstnameText = firstnameInput.findViewById<TextInputEditText>(R.id.editText)
        firstnameText.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        viewModel.firstname.observe(viewLifecycleOwner) {
            if (!it.equals(firstnameText.text.toString())) {
                firstnameText.setText(it)
            }        }
        firstnameText.addTextChangedListener { firstname ->
            //viewModel.setFirstname(firstname.toString())
            viewModel.validateFirstName(firstname.toString())
            checkTextReady()
        }
        viewModel.firstNameError.observe(viewLifecycleOwner){
            firstnameText.error = it
        }

        var lastnameInput = linearLayout.findViewById<TextInputLayout>(R.id.lastname_input)
        lastnameInput.hint = getString(R.string.lastname_hint)
        this.lastnameText = lastnameInput.findViewById<TextInputEditText>(R.id.editText)
        lastnameText.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        viewModel.lastname.observe(viewLifecycleOwner) {
            if(!it.equals(lastnameText.text.toString())){
                lastnameText.setText(it)
            }
        }
        lastnameText.addTextChangedListener { lastname ->
            //viewModel.setLastname(lastname.toString())
            viewModel.validateLastName(lastname.toString())
            checkTextReady()
        }
        viewModel.lastNameError.observe(viewLifecycleOwner){
            lastnameText.error = it
        }

        var birthdateInput = linearLayout.findViewById<TextInputLayout>(R.id.birthdate)
        birthdateInput.hint = getString(R.string.birthdate_hint)
        this.birthdateInputEdit = birthdateInput.findViewById<TextInputEditText>(R.id.editText)
        viewModel.birthdateString.observe(viewLifecycleOwner) {
            if(!it.equals(birthdateInputEdit.text.toString())){
                birthdateInputEdit.setText(it)
            }
        }
        birthdateInputEdit.inputType = InputType.TYPE_CLASS_DATETIME
        birthdateInputEdit.focusable = NOT_FOCUSABLE
        /*birthdateInputEdit.addTextChangedListener { date ->
            viewModel.setBirthdateString(date.toString())
            checkTextReady()
        }*/
        birthdateInputEdit.setOnClickListener {
            val today = MaterialDatePicker.todayInUtcMilliseconds() // Timestamp d'aujourd'hui
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

            calendar.set(1900, Calendar.JANUARY, 1)
            val minDate = calendar.timeInMillis

            val maxDate = today

            val constraints = CalendarConstraints.Builder()
                .setStart(minDate) // Définit la date minimale
                .setEnd(maxDate)   // Définit la date maximale
                .setValidator(DateValidatorPointForward.from(minDate)) // Assure que les dates sélectionnées sont dans la plage
                .build()
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Date de naissance")
                .setCalendarConstraints(constraints)
                .build();
            datePicker.addOnPositiveButtonClickListener{selection ->
                run {
                    // Transformation de la date en LocalDate
                    date = Instant.ofEpochMilli(selection).atZone(ZoneId.systemDefault()).toLocalDate()
                    viewModel.setBirthdate(date)
                    //Log.d("DATE", date.toString())
                    val dateString = String.format(
                        Locale.getDefault(),
                        "%02d/%02d/%04d",
                        date.dayOfMonth,
                        date.monthValue,
                        date.year)
                    birthdateInputEdit.setText(dateString)
                }
            }
            datePicker.show(requireActivity().supportFragmentManager, "MATERIAL_DATE_PICKER")
        }


        var phoneInput = linearLayout.findViewById<TextInputLayout>(R.id.phone_input)
        phoneInput.hint = getString(R.string.phone_hint)
        this.phoneText = phoneInput.findViewById<TextInputEditText>(R.id.editText)
        phoneText.inputType = InputType.TYPE_CLASS_PHONE
        viewModel.phone.observe(viewLifecycleOwner) {
            if(!it.equals(phoneText.text.toString())){
                phoneText.setText(it)
            }
        }
        phoneText.addTextChangedListener { phone ->
            //viewModel.setPhone(phone.toString())
            viewModel.validatePhone(phone.toString())
            checkTextReady()
        }
        viewModel.phoneError.observe(viewLifecycleOwner){
            phoneText.error = it
        }

        var mailInput = linearLayout.findViewById<TextInputLayout>(R.id.mail_input)
        mailInput.hint = getString(R.string.mail_hint)
        this.mailText = mailInput.findViewById<TextInputEditText>(R.id.editText)
        mailText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        viewModel.email.observe(viewLifecycleOwner) {
           if(!it.equals(mailText.text.toString())) {
                mailText.setText(it)
            }
        }
        mailText.addTextChangedListener { mail->
            //viewModel.setEmail(mail.toString())
            viewModel.validateEmail(mail.toString())
            checkTextReady()
        }
        viewModel.emailError.observe(viewLifecycleOwner){
            mailText.error = it
        }

        submitButton = parent.findViewById<ExtendedFloatingActionButton>(R.id.submitSignUpButton)
        submitButton.setOnClickListener { lifecycleScope.launch {
            Log.d("Fragment", "Signup clicked")
            viewModel.setLogin(loginText.text.toString())
            viewModel.setPassword(passwordText.text.toString())
            viewModel.setFirstname(firstnameText.text.toString())
            viewModel.setLastname(lastnameText.text.toString())
            viewModel.setBirthdateString(birthdateInputEdit.text.toString())
            viewModel.setBirthdate(date)
            viewModel.setPhone(phoneText.text.toString())
            viewModel.setEmail(mailText.text.toString())
            signupListener?.signup()
        } }

        val switchText = parent.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.switchSignUpText)

        switchText.setOnClickListener { lifecycleScope.launch {
            Log.d("Fragment", "SwitchToLogin clicked")
            signupListener?.onSwitchToLogin()
        }
        }
        val chipGroup = linearLayout.findViewById<com.google.android.material.chip.ChipGroup>(R.id.chipGroup)

        chipGroup.setOnCheckedStateChangeListener{ group, checkedIds ->
            selectedChips.clear()
            selectedChips.addAll(checkedIds.map { id ->
                val chip = group.findViewById<Chip>(id)
                chip.text.toString()
            })
            isChipsReady = selectedChips.isNotEmpty()
            Log.d("chips", ""+selectedChips)
            viewModel.setInterests(selectedChips)
            isButtonReady()
        }
    }

    private fun isButtonReady(){
        submitButton.isEnabled = isTextReady && isChipsReady
    }

    private fun checkTextReady(){
        this.isTextReady = loginText.text!!.isNotBlank() &&
                loginText.error == null &&
                passwordText.text!!.isNotBlank()&&
                passwordText.error == null&&
                firstnameText.text!!.isNotBlank()&&
                firstnameText.error == null&&
                lastnameText.text!!.isNotBlank()&&
                lastnameText.error == null&&
                birthdateInputEdit.text!!.isNotBlank()&&
                phoneText.text!!.isNotBlank()&&
                phoneText.error==null&&
                mailText.text!!.isNotBlank()&&
                mailText.error == null

        //Log.d("ready", ""+this.isTextReady)
        isButtonReady()
    }

}