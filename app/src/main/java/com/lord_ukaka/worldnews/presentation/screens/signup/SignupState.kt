package com.lord_ukaka.worldnews.presentation.screens.signup

data class SignupState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val emailError: String? = null,
    val phoneNUmber: String = "",
    val phoneNumberError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatPassword: String = "",
    val repeatPasswordError: String? = null,
    val isTermsAccepted: Boolean = false,
    val termsError: String? = null,
    val gender: String = "",
    val country: String = ""
)
