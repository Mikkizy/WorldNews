package com.lord_ukaka.worldnews.core.sealed

import com.lord_ukaka.worldnews.core.sealed.Error as ErrorBody

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    object Success : UiState()
    data class Error(val error: ErrorBody) : UiState()
}
