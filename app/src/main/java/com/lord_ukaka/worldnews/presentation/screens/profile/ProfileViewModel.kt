package com.lord_ukaka.worldnews.presentation.screens.profile

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lord_ukaka.worldnews.core.utils.LOGGED_USER_ID
import com.lord_ukaka.worldnews.core.utils.dataStore
import com.lord_ukaka.worldnews.domain.models.User
import com.lord_ukaka.worldnews.domain.usecases.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUsecase: UserUsecase,
    @ApplicationContext private val context: Context
): ViewModel(){

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    var loggedUserId by Delegates.notNull<Int>()

    private fun getUser() = viewModelScope.launch {
        context.dataStore.data.map {
            it[LOGGED_USER_ID]
        }.collectLatest {
            loggedUserId = it!!
        }
    }

    var user: User? = null

    init {
        viewModelScope.launch{
            getUser()
            user = userUsecase.getUser(loggedUserId)
            _state.value = _state.value.copy(
                username = user?.firstName + "" + user?.lastName,
                email = user?.email ?: "",
                phoneNumber = user?.phone!!
            )
        }
    }
}