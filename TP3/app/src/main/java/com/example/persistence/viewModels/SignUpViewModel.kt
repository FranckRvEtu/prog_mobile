package com.example.persistence.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.persistence.entities.User
import java.time.LocalDate

class SignUpViewModel : ViewModel() {

    private val _login = MutableLiveData<String>()
    val login : LiveData<String> = _login

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _firstname = MutableLiveData<String>()
    val firstname : LiveData<String> = _firstname

    private val _lastname = MutableLiveData<String>()
    val lastname : LiveData<String> = _lastname

    private val _birthdate = MutableLiveData<LocalDate>()
    val birthdate : LiveData<LocalDate> = _birthdate

    private val _birthdateString = MutableLiveData<String>()
    val birthdateString : LiveData<String> = _birthdateString

    private val _phone = MutableLiveData<String>()
    val phone : LiveData<String> = _phone

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _interests = MutableLiveData<List<String>>()
    val interests : LiveData<List<String>> = _interests

    private val _loginError = MutableLiveData<String?>()
    val loginError: LiveData<String?> = _loginError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _firstNameError = MutableLiveData<String?>()
    val firstNameError: LiveData<String?> = _firstNameError

    private val _lastNameError = MutableLiveData<String?>()
    val lastNameError: LiveData<String?> = _lastNameError

    private val _phoneError = MutableLiveData<String?>()
    val phoneError: LiveData<String?> = _phoneError

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    fun validateLogin(login: String) {
        val regex = Regex("^[A-Za-z][A-Za-z0-9]{0,9}$")
        _loginError.value = if (!login.matches(regex)) "Identifiant incorrecte" else null
    }

    fun validatePassword(password: String) {
        _passwordError.value = if (password.length < 6) "Mot de passe trop court" else null
    }

    fun validateFirstName(firstname: String) {
        val regex = Regex("^[A-Za-z]+$")
        _firstNameError.value = if (!firstname.matches(regex)) "Caractère invalide" else null
    }

    fun validateLastName(lastname: String) {
        val regex = Regex("^[A-Za-z]+$")
        _lastNameError.value = if (!lastname.matches(regex)) "Caractère invalide" else null
    }

    fun validatePhone(phone: String) {
        val regex = Regex("^[0-9]{10}$")
        _phoneError.value = if (!phone.matches(regex)) "Numéro de téléphone incorrect" else null
    }

    fun validateEmail(email: String) {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        _emailError.value = if (!email.matches(regex)) "Adresse email incorrecte" else null
    }

    fun setLogin(login: String) {
        _login.value = login
    }
    fun setPassword(password: String) {
        _password.value = password
    }
    fun setFirstname(firstname: String) {
        _firstname.value = firstname
    }
    fun setLastname(lastname: String) {
        _lastname.value = lastname
    }
    fun setBirthdate(birthdate: LocalDate) {
        _birthdate.value = birthdate
    }
    fun setBirthdateString(birthdateString: String) {
        _birthdateString.value = birthdateString
    }
    fun setPhone(phone: String) {
        _phone.value = phone
    }
    fun setEmail(email: String) {
        _email.value = email
    }
    fun setInterests(interests: List<String>) {
        _interests.value = interests
    }

    fun toUser(): User {
        val user =  User(login.value.toString(),
            password.value.toString(),
            firstname.value.toString(),
            lastname.value.toString(),
            birthdate.value!!,
            phone.value.toString(),
            email.value.toString(),
            interests.value!!)
        return user
    }
}