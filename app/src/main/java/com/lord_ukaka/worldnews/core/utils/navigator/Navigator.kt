package com.lord_ukaka.worldnews.core.utils.navigator

import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.Flow

interface Navigator {
    fun navigateUp(): Boolean
    fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit = { launchSingleTop = true }): Boolean
    fun navigate(route: NavDirections, options: NavOptions? = null): Boolean
    val destinations: Flow<NavigatorEvent>
}