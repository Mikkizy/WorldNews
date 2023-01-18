package com.lord_ukaka.worldnews.presentation.screens.login

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lord_ukaka.worldnews.core.sealed.DataResponse
import com.lord_ukaka.worldnews.core.sealed.Error
import com.lord_ukaka.worldnews.core.sealed.UiState
import com.lord_ukaka.worldnews.core.utils.LOGGED_USER_ID
import com.lord_ukaka.worldnews.core.utils.UserPref
import com.lord_ukaka.worldnews.core.utils.dataStore
import com.lord_ukaka.worldnews.domain.usecases.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUsecase: UserUsecase,
    @ApplicationContext private val context: Context
): ViewModel() {

    private var _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> = _uiState

    private var _email = mutableStateOf<String?>("")
    val email: State<String?> = _email

    private var _password = mutableStateOf<String?>("")
    val password: State<String?> = _password

    fun updateEmail(email: String?) {
        _email.value = email
    }

    fun updatePassword(password: String?) {
        _password.value = password
    }

    fun authenticateUser(
        email: String,
        password: String,
        onAuthenticated: () -> Unit,
        onAuthenticationFailed: () -> Unit,
    ) {
        if (email.isBlank() || password.isBlank()) onAuthenticationFailed()
        else {
            _uiState.value = UiState.Loading
            /** We will use the coroutine so that we don't block our dear : The UiThread */
            viewModelScope.launch {
                delay(3000)
                userUsecase.signInUser(
                    email = email,
                    password = password,
                ).let {
                    when (it) {
                        is DataResponse.Success -> {
                            it.data?.let { user ->
                                /** Authenticated successfully */
                                _uiState.value = UiState.Success
                                UserPref.updateUser(user = user)
                                /** save user id */
                                saveUserIdToPreferences(userId = user.userId!!)
                                onAuthenticated()
                            }
                        }
                        is DataResponse.Error -> {
                            /** An error occurred while authenticating */
                            _uiState.value = UiState.Error(error = Error.Network)
                            onAuthenticationFailed()
                        }
                        else -> {
                            _uiState.value = UiState.Idle
                        }
                    }
                }
            }
        }
    }

    private suspend fun saveUserIdToPreferences(userId: Int) {
        context.dataStore.edit {
            it[LOGGED_USER_ID] = userId
        }
    }
}