package com.lord_ukaka.worldnews.presentation.screens.onboarding

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.lord_ukaka.worldnews.R
import com.lord_ukaka.worldnews.core.utils.APP_LAUNCHED
import com.lord_ukaka.worldnews.core.utils.dataStore
import com.lord_ukaka.worldnews.core.utils.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val navigator: Navigator
) : ViewModel(){

    @OptIn(ExperimentalPagerApi::class)
    private var _pagerState = mutableStateOf(PagerState())
    @OptIn(ExperimentalPagerApi::class)
    val pagerState: State<PagerState> = _pagerState

    fun updateLaunchState(context: Context) {
        viewModelScope.launch {
            context.dataStore.edit {
                it[APP_LAUNCHED] = true
            }
        }
    }

    val pagerDataItems = listOf(
        OnboardingState(
            title = "Welcome to World News",
            image = R.drawable.ic_breaking_news_2,
            description = "Get interesting and authentic breaking news on various topics"
        ),
        OnboardingState(
            title = "Anywhere, Any Place!",
            image = R.drawable.ic_world,
            description = "Read your favorite trending news from across the globe"
        ),
        OnboardingState(
            title = "Can't Read Now?",
            image = R.drawable.ic_save_news_1,
            description = "Save your favorite topics and read up whenever you're free to."
        ),
        OnboardingState(
            title = "Don't Want to Miss Out?",
            image = R.drawable.ic_follow_favorite,
            description = "Follow your favorite news sources and journalists to stay up to date with their articles."
        )
    )

    private val input = Input()

    data class Input(
        val pagerData: MutableSharedFlow<List<OnboardingState>> = MutableSharedFlow(1)
    ) {
        fun setPagerData(data: List<OnboardingState>) = pagerData.tryEmit(data)
    }

    init {
        input.setPagerData(pagerDataItems)
    }

}