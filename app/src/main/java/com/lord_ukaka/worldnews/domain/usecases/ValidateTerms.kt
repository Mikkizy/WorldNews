package com.lord_ukaka.worldnews.domain.usecases

import android.util.Patterns

class ValidateTerms {

    operator fun invoke(acceptedTerms: Boolean): ValidationResult {
        if (!acceptedTerms) {
            return ValidationResult(
                successful = false,
                errorMessage = "Kindly accept terms"
            )
        }
        return ValidationResult(successful = true)
    }
}