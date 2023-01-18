package com.lord_ukaka.worldnews.presentation.screens.splash

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lord_ukaka.worldnews.core.utils.APP_LAUNCHED
import com.lord_ukaka.worldnews.core.utils.LOGGED_USER_ID
import com.lord_ukaka.worldnews.core.utils.UserPref
import com.lord_ukaka.worldnews.core.utils.dataStore
import com.lord_ukaka.worldnews.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class SplashViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: NewsRepository
    ): ViewModel() {

    val hasAppBeenLaunchedBefore = context.dataStore.data.map {
        it[APP_LAUNCHED] ?: false
    }

    val loggedUserId = context.dataStore.data.map {
        it[LOGGED_USER_ID]
    }

    fun verifyLoggedUser(userId: Int, onVerify: () -> Unit) {
        viewModelScope.launch {
            repository.getUser(userId)?.let {
                UserPref.updateUser(it)
            }
            onVerify()
        }
    }
}