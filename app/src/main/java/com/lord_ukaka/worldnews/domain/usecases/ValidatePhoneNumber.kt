package com.lord_ukaka.worldnews.domain.usecases

import android.util.Patterns

class ValidatePhoneNumber {

    operator fun invoke(phoneNumber: String): ValidationResult {
        if (phoneNumber.isNotBlank()) {
            val phoneNumberFormat = phoneNumber.length in 6..13 &&
                    Patterns.PHONE.matcher(phoneNumber).matches()
            if (!phoneNumberFormat) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "Invalid Phone Number"
                )
            }
        }

        return ValidationResult(successful = true)
    }
}