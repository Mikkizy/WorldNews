package com.lord_ukaka.worldnews.domain.usecases

import android.util.Patterns
import java.util.regex.Pattern

class ValidatePassword {

    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password cannot be empty"
            )
        }
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password needs to contain at least 8 characters"
            )
        }

        val pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(password)
        val passwordFormat = password.any{ it.isLetter() } && password.any { it.isDigit() }
                && password.any { it.isUpperCase() } && matcher.find()

        if (!passwordFormat) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password must contain at least an Uppercase letter, a digit and a special character"
            )
        }
        return ValidationResult(successful = true)
    }
}