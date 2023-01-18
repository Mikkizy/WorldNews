package com.lord_ukaka.worldnews.domain.usecases

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
