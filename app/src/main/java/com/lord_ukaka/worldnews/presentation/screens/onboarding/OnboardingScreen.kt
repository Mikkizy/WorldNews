package com.lord_ukaka.worldnews.presentation.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.lord_ukaka.worldnews.core.components.CustomButton
import com.lord_ukaka.worldnews.core.sealed.Screen
import com.lord_ukaka.worldnews.core.utils.Dimension
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<OnboardingViewModel>()
    val data = remember {
        viewModel.pagerDataItems
    }
    val state = remember {
        viewModel.pagerState
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.updateLaunchState(context)
    }

   Box(modifier = Modifier
       .fillMaxSize()
       .padding(Dimension.pagePadding)) {
        OnboardingPager(
            modifier = Modifier,
            data = data,
            state = state.value,
            navController = navController
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingPager(
    modifier: Modifier,
    data: List<OnboardingState>,
    state: PagerState,
    navController: NavController,
) {
    val scope = rememberCoroutineScope()
        Column(
            modifier = modifier
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimension.pagePadding)
        ) {
            HorizontalPager(
                count = data.size,
                state = state,
                modifier = Modifier.wrapContentHeight()
            ) { page ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = Dimension.pagePadding.times(1.5f))
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = data[page].title,
                        style = MaterialTheme.typography.h4,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painterResource(id = data[page].image),
                        contentDescription = data[page].title,
                        modifier = Modifier
                            .size(200.dp, 150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = data[page].description,
                        style = MaterialTheme.typography.body2,
                        color = Color.Black
                    )
                }
            }
            HorizontalPagerIndicator(pagerState = state)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val isLastPage = state.currentPage == (data.size - 1)
                val nextStart = if (isLastPage) "Start" else "Next"
                CustomButton(
                    modifier = Modifier
                        .width(Dimension.extraLargeIcon),
                    buttonColor = Color.White,
                    contentColor = Color.Blue,
                    text = "Skip"
                ) {
                    navController.navigate(Screen.Signup.route) {
                        popUpTo(Screen.Onboard.route) {
                            inclusive = true
                        }
                    }
                }
                CustomButton(
                    modifier = Modifier
                        .width(Dimension.extraLargeIcon),
                    buttonColor = Color.Blue,
                    contentColor = Color.White,
                    text = nextStart
                ) {
                    if (isLastPage) {
                        navController.navigate(Screen.Signup.route) {
                            popUpTo(Screen.Onboard.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        scope.launch {
                            state.scrollToPage(state.currentPage + 1)
                        }
                    }
                }
            }
        }


}