package com.lord_ukaka.worldnews.domain.usecases

import java.util.regex.Pattern

class ValidateRepeatedPassword {

    operator fun invoke(password: String, repeatedPassword: String): ValidationResult {
        if (repeatedPassword != password) {
            return ValidationResult(
                successful = false,
                errorMessage = "Passwords do not match"
            )
        }
        return ValidationResult(successful = true)
    }
}