package com.example.museet.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.museet.data.Result

import com.example.museet.R
import com.example.museet.data.MuseetRepository

class SignupViewModel(private val repository: MuseetRepository) : ViewModel() {

    private val _signupForm = MutableLiveData<SignupFormState>()
    val signupFormState: LiveData<SignupFormState> = _signupForm

    private val _signupResult = MutableLiveData<SignupResult>()
    val signupResult: LiveData<SignupResult> = _signupResult

    fun signup(username: String?,location:String?,phone:String?,youtubeLink: String?,password: String?,userType:String?,name:String?,instruments:MutableList<String>) {
        // can be launched in a separate asynchronous job
        repository.register(username, location,phone,youtubeLink,password,userType,name,instruments)

    }

    fun signupDataChanged(username: String, email:String,phone:String,youtubeLink: String,password: String,name:String) {
        if (!isEmailValid(username)) {
            _signupForm.value = SignupFormState(usernameError = R.string.invalid_email)
        }
        else if(!isPhoneNumberValid(phone))
        {
            _signupForm.value = SignupFormState(phoneError = R.string.invalid_phone)}
        else if(!isYoutubeLinkValid(youtubeLink))
        {
            _signupForm.value = SignupFormState(youtubeLinkError = R.string.invalid_youtube)}
        else if (!isPasswordValid(password)) {
            _signupForm.value = SignupFormState(passwordError = R.string.invalid_password)
        }
        else if (!isEmailValid(email))
        {  _signupForm.value = SignupFormState(locationError = R.string.invalid_location)}
        else if(!isUserNameValid(name))
        {_signupForm.value = SignupFormState(nameError = R.string.invalid_username)}
        else {
            _signupForm.value = SignupFormState(isDataValid = true)
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