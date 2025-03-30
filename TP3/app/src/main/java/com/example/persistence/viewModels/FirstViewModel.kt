package com.example.persistence.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FirstViewModel : ViewModel() {
    private val _currentFragment = MutableLiveData<String>()
    val currentFragment: LiveData<String> = _currentFragment

    fun setCurrentFragment(fragmentTag: String) {
        _currentFragment.value = fragmentTag
    }
}
