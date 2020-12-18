package com.example.museet.ui.signup

/**
 * Data validation state of the login form.
 */
data class SignupFormState(val usernameError: Int? = null,
                           val passwordError: Int? = null,
                           val phoneError: Int? = null,
                           val youtubeLinkError: Int? = null,
                           val nameError: Int? = null,
                           val typeError: Int?=null,
                           val locationError: Int?=null,
                           val userTypeError: Int?=null,
                           val isDataValid: Boolean = false)