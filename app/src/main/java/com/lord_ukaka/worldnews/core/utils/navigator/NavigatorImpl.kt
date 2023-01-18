package com.lord_ukaka.worldnews.core.utils.navigator

import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NavigatorImpl @Inject constructor() : Navigator {

    private val navigationEvents = Channel<NavigatorEvent>()
    override val destinations = navigationEvents.receiveAsFlow()

    override fun navigateUp(): Boolean = navigationEvents.trySend(NavigatorEvent.NavigateUp).isSuccess
    override fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit): Boolean = navigationEvents.trySend(NavigatorEvent.ComposeDirections(route, builder)).isSuccess
    override fun navigate(route: NavDirections, options: NavOptions?): Boolean = navigationEvents.trySend(NavigatorEvent.JetPackDirections(route, options)).isSuccess
}