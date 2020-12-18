package com.example.museet.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.museet.R
import com.example.museet.data.MuseetRepository

class HomeViewModel (private val repository: MuseetRepository) : ViewModel() {

    private val _homeForm = MutableLiveData<HomeFormState>()
    val homeFormState: LiveData<HomeFormState> = _homeForm


    fun homeDataChanged(location: String) {
        if (!isLocationValid(location)) {
            _homeForm.value = HomeFormState(locationError = R.string.invalid_location)
        }  else {
            _homeForm.value = HomeFormState(isDataValid = true)
        }
    }

    private fun isLocationValid(location: String): Boolean {
        return location.length > 2
    }
}