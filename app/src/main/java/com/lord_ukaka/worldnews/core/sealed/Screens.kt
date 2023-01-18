package com.lord_ukaka.worldnews.core.sealed

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lord_ukaka.worldnews.R

sealed class Screen(
    var route: String,
    @StringRes var title: Int? = null,
    @DrawableRes val icon: Int? = null
) {
    object Splash : Screen(
        route = "splash",
    )
    object Onboard : Screen(
        route = "onboard",
        title = R.string.onboard,
    )
    object Signup : Screen(
        route = "signup",
        title = R.string.signup,
    )
    object Login : Screen(
        route = "login",
        title = R.string.login,
    )
    object Options: Screen(
        route = "options",
        title = R.string.options
    )
    object Search: Screen(
        route = "search",
        title = R.string.search
    )
    object Article: Screen(
        route = "article",
        title = R.string.artcle
    )
    object Home: Screen(
        route = "home",
        title = R.string.home,
        icon = R.drawable.ic_home
    )
    object Profile: Screen(
        route = "profile",
        title = R.string.profile,
        icon = R.drawable.ic_person
    )
    object Saved: Screen(
        route = "saved",
        title = R.string.saved,
        icon = R.drawable.ic_favorite
    )
}
