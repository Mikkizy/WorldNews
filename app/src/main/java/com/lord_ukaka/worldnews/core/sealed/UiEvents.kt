package com.lord_ukaka.worldnews.core.sealed

sealed class UiEvents {
    data class ShowSnackBar(val message: String): UiEvents()
}
