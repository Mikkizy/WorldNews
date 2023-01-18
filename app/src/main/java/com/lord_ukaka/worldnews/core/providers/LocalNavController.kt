package com.lord_ukaka.worldnews.core.providers

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavHost = compositionLocalOf <NavHostController>{ error("NavController has not been provided") }