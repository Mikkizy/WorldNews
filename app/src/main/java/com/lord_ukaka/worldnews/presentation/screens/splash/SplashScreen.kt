package com.lord_ukaka.worldnews.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lord_ukaka.worldnews.R
import com.lord_ukaka.worldnews.core.sealed.Screen
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    OnSplashFinished: (navigateTo: Screen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val hasAppBeenLaunchedBefore by splashViewModel.hasAppBeenLaunchedBefore.collectAsState(
            initial = false
        )
        
        val loggedUserId by splashViewModel.loggedUserId.collectAsState(initial = null)

        LaunchedEffect(key1 = Unit) {
            delay(3000)
            if (hasAppBeenLaunchedBefore) {
                loggedUserId.whatIfNotNull(
                    whatIf = {
                        splashViewModel.verifyLoggedUser(it){
                            OnSplashFinished(Screen.Home)
                        }
                    },
                    whatIfNot = {
                        OnSplashFinished(Screen.Home)
                    }
                )
            } else {
                OnSplashFinished(Screen.Onboard)
            }
        }

        val appName = stringResource(id = R.string.app_name)
        Text(
            text = buildAnnotatedString {
                append(appName.take(5))
                withStyle(
                    style = MaterialTheme.typography.h1
                        .copy(
                            fontSize = 64.sp,
                            color = MaterialTheme.colors.primary,
                            fontFamily = FontFamily.Cursive
                        ).toSpanStyle()
                ) {
                    append(appName.takeLast(4))
                }
            },
            style = MaterialTheme.typography.h1
                .copy(
                    fontSize = 64.sp,
                    color = MaterialTheme.colors.secondary,
                    fontFamily = FontFamily.Cursive
                )
        )
    }
}