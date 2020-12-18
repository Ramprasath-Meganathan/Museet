package com.example.museet.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.museet.data.MuseetRepository

/**
 * ViewModel provider factory to instantiate HomeViewModel.
 * Required given HomeViewModel has a non-empty constructor
 */
class HomeViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                repository = MuseetRepository()
            ) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                repository = MuseetRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}