package com.example.museet.ui.signup

/**
 * Authentication result : success (user details) or error message.
 */
data class SignupResult(
        val success: SignupUserView? = null,
        val error: Int? = null
)