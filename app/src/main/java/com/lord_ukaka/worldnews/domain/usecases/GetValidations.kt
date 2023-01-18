package com.lord_ukaka.worldnews.domain.usecases

data class GetValidations(
    val validateEmail: ValidateEmail,
    val validatePassword: ValidatePassword,
    val validateRepeatedPassword: ValidateRepeatedPassword,
    val validateTerms: ValidateTerms,
    val validatePhoneNumber: ValidatePhoneNumber
)
