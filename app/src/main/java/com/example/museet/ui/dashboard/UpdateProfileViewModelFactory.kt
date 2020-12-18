package com.example.museet.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.museet.data.MuseetRepository

/**
 * ViewModel provider factory to instantiate HomeViewModel.
 * Required given updateprofile view model has a non-empty constructor
 */
class UpdateProfileViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                repository = MuseetRepository()
            ) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                repository = MuseetRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}