package com.example.museet.ui.dashboard

/**
 * Data validation state of the login form.
 */
data class ProfileFormState(
                           val nameError: Int? = null,
                           val phoneError: Int? = null,
                           val youtubeLinkError: Int? = null,
//                           val typeError: Int?=null,
                           val locationError: Int?=null,
//                           val userTypeError: Int?=null,
                           val isDataValid: Boolean = false)