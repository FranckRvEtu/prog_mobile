package com.example.persistence.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlanningViewModel : ViewModel() {
    private val _currentFragment = MutableLiveData<String>()
    val currentFragment: LiveData<String> = _currentFragment

    fun setCurrentFragment(fragmentTag: String) {
        _currentFragment.value = fragmentTag
    }

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    fun setUserId(userId: String) {
        _userId.value = userId
    }

    private val _first = MutableLiveData<String>()
    val first: LiveData<String> = _first
    fun setFirst(first: String) {
        _first.value = first
    }
    private val _second = MutableLiveData<String>()
    val second: LiveData<String> = _second
    fun setSecond(second: String) {
        _second.value = second
    }
    private val _third = MutableLiveData<String>()
    val third: LiveData<String> = _third
    fun setThird(third: String) {
        _third.value = third
    }
    private val _fourth = MutableLiveData<String>()
    val fourth: LiveData<String> = _fourth
    fun setFourth(fourth: String) {
        _fourth.value = fourth
    }


}