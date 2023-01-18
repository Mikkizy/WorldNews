package com.lord_ukaka.worldnews.presentation.screens.signup

sealed class SignupEvents {
    data class EnteredFirstName(val firstName: String): SignupEvents()
    data class EnteredLastName(val lastName: String): SignupEvents()
    data class EnteredCountry(val country: String): SignupEvents()
    data class EnteredGender(val gender: String): SignupEvents()
    data class EnteredEmail(val email: String): SignupEvents()
    data class EnteredPassword(val password: String): SignupEvents()
    data class EnteredRepeatedPassword(val repeatedPassword: String): SignupEvents()
    data class EnteredPhoneNumber(val phoneNumber: String): SignupEvents()
    data class AcceptedTerms(val acceptedTerms: Boolean): SignupEvents()
    object OnClickSignIn: SignupEvents()
}
