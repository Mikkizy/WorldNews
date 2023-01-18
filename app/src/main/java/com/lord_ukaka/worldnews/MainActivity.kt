package com.lord_ukaka.worldnews

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lord_ukaka.worldnews.core.components.BottomNav
import com.lord_ukaka.worldnews.core.components.getActiveRoute
import com.lord_ukaka.worldnews.core.sealed.Screen
import com.lord_ukaka.worldnews.core.utils.getJsonData
import com.lord_ukaka.worldnews.domain.models.CountryDetails
import com.lord_ukaka.worldnews.presentation.screens.article.ArticleScreen
import com.lord_ukaka.worldnews.presentation.screens.home.HomeScreen
import com.lord_ukaka.worldnews.presentation.screens.login.LoginScreen
import com.lord_ukaka.worldnews.presentation.screens.onboarding.OnboardingScreen
import com.lord_ukaka.worldnews.presentation.screens.profile.ProfileScreen
import com.lord_ukaka.worldnews.presentation.screens.saved.SavedArticleScreen
import com.lord_ukaka.worldnews.presentation.screens.search.SearchScreen
import com.lord_ukaka.worldnews.presentation.screens.signup.SignupScreen
import com.lord_ukaka.worldnews.presentation.screens.splash.SplashScreen
import com.lord_ukaka.worldnews.ui.theme.WorldNewsTheme
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Type

@OptIn(ExperimentalMaterialApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            //Set the status bar color of our app
            window.statusBarColor = Color.Red.toArgb()

            WorldNewsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val bottomNavDestinations = remember {
                        listOf(Screen.Home, Screen.Search, Screen.Saved, Screen.Profile)
                    }
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    Column {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Splash.route
                        ) {
                            composable(route = Screen.Splash.route) {
                                SplashScreen(OnSplashFinished = { nextDestination ->
                                    navController.navigate(nextDestination.route) {
                                        //We do not want to be able to go back to the splash screen from our backstack
                                        popUpTo(Screen.Splash.route) {
                                            inclusive = true
                                        }
                                    }
                                })
                            }
                            composable(route = Screen.Onboard.route) {
                                OnboardingScreen(navController)
                            }
                            composable(route = Screen.Signup.route) {
                                SignupScreen(
                                    onUserAccountCreated = {
                                                           navController.navigate(Screen.Login.route)
                                    },
                                    navController = navController
                                )
                            }
                            composable(route = Screen.Login.route) {
                                LoginScreen(
                                    navController = navController,
                                    onUserAuthenticated = { navController.navigate(Screen.Home.route) },
                                    onToastRequested = { message ->
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    }
                                )
                            }
                            composable(route = Screen.Home.route) {
                                HomeScreen(navController)
                            }
                            composable(route = Screen.Search.route) {
                                SearchScreen(navController)
                            }
                            composable(route = Screen.Saved.route) {
                                SavedArticleScreen(navController)
                            }
                            composable(route = Screen.Profile.route) {
                                ProfileScreen(navController)
                            }
                            composable(
                                route = Screen.Article.route + "articleId={articleId}",
                                arguments = listOf(navArgument(name = "articleId"){
                                    type = NavType.IntType
                                })
                            ) {
                                ArticleScreen(navController)
                            }
                        }

                        val currentRouteAsState = getActiveRoute(navController = navController)
                        if (currentRouteAsState in bottomNavDestinations.map { it.route }) {
                            BottomNav(
                                destinations = bottomNavDestinations,
                                navController = navController,
                                onDestinationClicked = {
                                    navController.navigate(it.route)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}