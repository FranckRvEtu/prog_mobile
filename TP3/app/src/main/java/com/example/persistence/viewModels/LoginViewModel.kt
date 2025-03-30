package com.example.persistence.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel(){
    private val _login = MutableLiveData<String>()
    val login : LiveData<String> = _login

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    fun setLogin(login: String) {
        _login.value = login
    }
    fun setPassword(password: String) {
        _password.value = password
    }
}