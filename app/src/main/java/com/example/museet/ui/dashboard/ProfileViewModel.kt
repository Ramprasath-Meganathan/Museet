package com.example.museet.ui.dashboard

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.museet.R
import com.example.museet.data.MuseetRepository
import com.example.museet.ui.signup.SignupFormState
import com.example.museet.ui.signup.SignupResult

class ProfileViewModel(private val repository: MuseetRepository) : ViewModel() {

    private val _profileUpdateForm = MutableLiveData<ProfileFormState>()
    val profileUpdateForm: LiveData<ProfileFormState> = _profileUpdateForm


    private val _text = MutableLiveData<String>().apply {
        value = "Update Profile"
    }


    val text: LiveData<String> = _text
    fun profileDataChanged(name:String,location:String,phone:String,youtubeLink: String) {
         if(!isUserNameValid(name))
        {_profileUpdateForm.value = ProfileFormState(nameError = R.string.invalid_username)}
        else if (!isUserNameValid(location))
        {  _profileUpdateForm.value = ProfileFormState(locationError = R.string.invalid_location)}
         if(!isPhoneNumberValid(phone))
        {
            _profileUpdateForm.value = ProfileFormState(phoneError = R.string.invalid_phone)}
        else if(!isYoutubeLinkValid(youtubeLink))
        {
            _profileUpdateForm.value = ProfileFormState(youtubeLinkError = R.string.invalid_youtube)}

        else {
            _profileUpdateForm.value = ProfileFormState(isDataValid = true)
        }
    }
    // A placeholder username validation check
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
    private fun isUserNameValid(userName: String): Boolean {
        return userName.length > 2
    }
    private fun isYoutubeLinkValid(youtubeLink: String): Boolean {
        return youtubeLink.length > 5
    }

    private fun isPhoneNumberValid(phone: String): Boolean {
        return phone.length in 9..11
    }

}