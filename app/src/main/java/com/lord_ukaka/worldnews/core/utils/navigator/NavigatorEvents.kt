package com.lord_ukaka.worldnews.core.utils.navigator

import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder

sealed class NavigatorEvent {
    object NavigateUp : NavigatorEvent()
    class ComposeDirections(
        val destination: String,
        val builder: NavOptionsBuilder.() -> Unit
    ) : NavigatorEvent()

    class JetPackDirections(
        val destination: NavDirections,
        val builder: NavOptions?
    ) : NavigatorEvent()
}
