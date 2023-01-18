package com.lord_ukaka.worldnews.domain.usecases

import android.util.Patterns

class ValidateEmail {

    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Email cannot be empty"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Invalid Email"
            )
        }
        return ValidationResult(successful = true)
    }
}